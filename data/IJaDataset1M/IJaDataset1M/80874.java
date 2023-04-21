package org.netxilia.spi.impl.formula;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.netxilia.api.concurrent.IFutureListener;
import org.netxilia.api.exception.EvaluationException;
import org.netxilia.api.exception.NetxiliaBusinessException;
import org.netxilia.api.exception.NotFoundException;
import org.netxilia.api.exception.StorageException;
import org.netxilia.api.formula.Formula;
import org.netxilia.api.formula.FormulaParsingException;
import org.netxilia.api.formula.IPreloadedFormulaContext;
import org.netxilia.api.impl.model.FutureListenerWithUser;
import org.netxilia.api.impl.user.ISpringUserService;
import org.netxilia.api.model.AbsoluteAlias;
import org.netxilia.api.model.Alias;
import org.netxilia.api.model.CellData;
import org.netxilia.api.model.ISheet;
import org.netxilia.api.model.SheetData;
import org.netxilia.api.reference.AreaReference;
import org.netxilia.api.reference.CellReference;
import org.netxilia.api.utils.Matrix;
import org.netxilia.api.value.ErrorValueType;
import org.netxilia.api.value.IGenericValue;
import org.netxilia.spi.formula.IFormulaParser;

/**
 * This context is used by the formula actor to return the values needed in the evaluations. The values are pre-filled
 * by browsing the formula's tree before evaluating the formula.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class AsyncPreloadedFormulaContextImpl implements IPreloadedFormulaContext {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AsyncPreloadedFormulaContextImpl.class);

    private final ISheet sheet;

    private final CellReference cell;

    private final Formula formula;

    private final boolean loadValues;

    private final Map<AreaReference, Matrix<CellData>> areaValues = new HashMap<AreaReference, Matrix<CellData>>();

    private final Map<AbsoluteAlias, AreaReference> aliases = new HashMap<AbsoluteAlias, AreaReference>();

    private final IFutureListener<SheetData> receiveAliasesListener;

    private final IFutureListener<Matrix<CellData>> receiveCellsListener;

    private final IFormulaParser formulaParser;

    private final Executor executor;

    private List<AbsoluteAlias> toLoadAliases;

    private List<AreaReference> toLoadReferences;

    private Runnable callWhenDone;

    public AsyncPreloadedFormulaContextImpl(ISheet sheet, CellReference cell, Formula formula, boolean loadValues, Executor executor, IFormulaParser formulaParser, ISpringUserService userService) {
        this.cell = cell;
        this.sheet = sheet;
        this.formula = formula;
        this.executor = executor;
        this.formulaParser = formulaParser;
        this.loadValues = loadValues;
        this.receiveAliasesListener = new FutureListenerWithUser<SheetData>(userService, new IFutureListener<SheetData>() {

            @Override
            public void ready(Future<SheetData> future) {
                try {
                    receivedAliases(future.get());
                } catch (Exception e) {
                    log.error("Cannot get value:" + e, e);
                }
            }
        });
        this.receiveCellsListener = new FutureListenerWithUser<Matrix<CellData>>(userService, new IFutureListener<Matrix<CellData>>() {

            @Override
            public void ready(Future<Matrix<CellData>> future) {
                try {
                    receivedCells(future.get());
                } catch (Exception e) {
                    log.error("Cannot get aliases:" + e, e);
                }
            }
        });
    }

    public void load(Runnable callWhenDone) throws StorageException, NotFoundException, FormulaParsingException {
        this.callWhenDone = callWhenDone;
        if (formula == null) {
            toLoadAliases = Collections.emptyList();
        } else {
            toLoadAliases = formulaParser.getAliases(formula, this);
        }
        loadNextData();
    }

    private void loadNextData() {
        try {
            if (toLoadAliases.size() > 0) {
                AbsoluteAlias alias = toLoadAliases.get(0);
                getSheet(alias.getSheetName()).receiveSheet().addListener(receiveAliasesListener, executor);
                return;
            }
            if (loadValues && formula != null) {
                if (toLoadReferences == null) {
                    try {
                        toLoadReferences = formulaParser.getDependencies(formula, this);
                    } catch (FormulaParsingException e) {
                    }
                }
                if (toLoadReferences.size() > 0) {
                    AreaReference ref = toLoadReferences.get(0);
                    getSheet(ref.getSheetName()).receiveCells(ref).addListener(receiveCellsListener, executor);
                    return;
                }
            }
        } catch (Exception e) {
            if (toLoadAliases.size() > 0) {
                toLoadAliases.remove(0);
            } else if (toLoadReferences.size() > 0) {
                toLoadReferences.remove(0);
            }
            loadNextData();
        }
        callWhenDone.run();
    }

    private void receivedAliases(SheetData sheetData) {
        toLoadAliases.remove(0);
        for (Map.Entry<Alias, AreaReference> entry : sheetData.getAliases().entrySet()) {
            aliases.put(new AbsoluteAlias(sheetData.getName(), entry.getKey()), entry.getValue());
        }
        loadNextData();
    }

    private void receivedCells(Matrix<CellData> cells) {
        AreaReference ref = toLoadReferences.remove(0);
        areaValues.put(ref, cells);
        loadNextData();
    }

    @Override
    public CellReference getCell() {
        return cell;
    }

    private ISheet getSheet(String sheetName) throws StorageException, NotFoundException {
        if (sheetName == null || sheetName.equals(sheet.getName())) {
            return sheet;
        }
        return sheet.getWorkbook().getSheet(sheetName);
    }

    @Override
    public AreaReference resolveAlias(AbsoluteAlias alias) {
        AreaReference ref = aliases.get(alias);
        if (ref == null) {
            return null;
        }
        return ref.withSheetName(alias.getSheetName());
    }

    @Override
    public ISheet getSheet() {
        return sheet;
    }

    @Override
    public IGenericValue getCellValue(CellReference ref) {
        Matrix<CellData> matrix = areaValues.get(new AreaReference(ref));
        if (matrix == null) {
            throw new EvaluationException(ErrorValueType.REF, new NetxiliaBusinessException("Reference " + ref + " was not found"));
        }
        return matrix.getRowCount() > 0 && matrix.getColumnCount() > 0 ? matrix.get(0, 0).getValue() : null;
    }

    @Override
    public Iterator<CellData> getCellIterator(AreaReference ref) {
        Matrix<CellData> matrix = areaValues.get(ref);
        return matrix != null ? matrix.iterator() : null;
    }
}
