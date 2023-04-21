package org.fudaa.dodico.ef.operation.overstressed;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntIterator;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.fudaa.ctulu.CtuluListSelection;
import org.fudaa.ctulu.CtuluListSelectionInterface;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.ProgressionUpdater;
import org.fudaa.dodico.ef.EfData;
import org.fudaa.dodico.ef.EfDataElement;
import org.fudaa.dodico.ef.EfElement;
import org.fudaa.dodico.ef.EfElementType;
import org.fudaa.dodico.ef.EfGridData;
import org.fudaa.dodico.ef.EfGridInterface;
import org.fudaa.dodico.ef.EfNeighborMesh;
import org.fudaa.dodico.ef.EfSegment;
import org.fudaa.dodico.ef.decorator.AbstractEfGridDataDecorator;
import org.fudaa.dodico.ef.impl.EfGridArray;
import org.fudaa.dodico.ef.operation.EfOperation;
import org.fudaa.dodico.ef.operation.EfOperationResult;
import org.fudaa.dodico.ef.operation.refine.EfOperationRefine;

public class EfOperationSwapSelectedEdge implements EfOperation {

    public static class EfGridDataFromSwapSelectedEdge extends AbstractEfGridDataDecorator {

        private final TIntIntHashMap modifiedElement;

        public EfGridDataFromSwapSelectedEdge(EfGridData init, EfGridInterface newGrid, TIntIntHashMap modifiedElement) {
            super(init, newGrid);
            this.modifiedElement = modifiedElement;
        }

        public EfData getData(CtuluVariable o, int timeIdx) throws IOException {
            EfData initData = this.getInit().getData(o, timeIdx);
            if (!initData.isElementData()) {
                return initData;
            }
            double[] newValues = initData.getValues();
            TIntIntIterator iterator = modifiedElement.iterator();
            while (iterator.hasNext()) {
                int elementIdx1 = iterator.key();
                int elementIdx2 = iterator.value();
                double newValue = (newValues[elementIdx1] + newValues[elementIdx2]) / 2d;
                newValues[elementIdx1] = newValue;
                newValues[elementIdx2] = newValue;
            }
            return new EfDataElement(newValues);
        }
    }

    private boolean stop;

    private EfGridData init;

    private Set<EfSegment> selectedEdge;

    private CtuluListSelectionInterface elementToRefined;

    private EfOperationRefine operationRefine;

    public EfOperationResult process(ProgressionInterface prog) {
        stop = false;
        EfGridData dataGrid = this.init;
        if (this.mustBeRefined(prog)) {
            this.operationRefine = new EfOperationRefine();
            this.operationRefine.setInitGridData(this.init);
            this.operationRefine.setSelectedElt(this.elementToRefined);
            dataGrid = this.operationRefine.process(prog).getGridData();
        }
        if (this.stop) return null;
        EfGridInterface grid = dataGrid.getGrid();
        EfElement[] elements = grid.getElts();
        Iterator<EfSegment> iterator = this.selectedEdge.iterator();
        EfNeighborMesh neighborMesh = EfNeighborMesh.compute(grid, prog);
        TIntIntHashMap modifiedElement = new TIntIntHashMap(this.selectedEdge.size() * 2);
        ProgressionUpdater updater = new ProgressionUpdater(prog);
        updater.majProgessionStateOnly("ef.swapSelectedEdge.msg");
        updater.setValue(10, this.selectedEdge.size());
        while (iterator.hasNext()) {
            if (this.stop) return null;
            EfSegment segment = iterator.next();
            int[] elementIdx = neighborMesh.getAdjacentMeshes(segment.getPt1Idx(), segment.getPt2Idx());
            EfElement element1 = grid.getElement(elementIdx[0]);
            EfElement element2 = grid.getElement(elementIdx[1]);
            if ((element1.getDefaultType() == EfElementType.T3) && (element2.getDefaultType() == EfElementType.T3)) {
                int notUsedPts1 = this.getNotUsedLocalPts(element1, segment.getPt1Idx(), segment.getPt2Idx());
                int notUsedPts2 = this.getNotUsedLocalPts(element2, segment.getPt1Idx(), segment.getPt2Idx());
                int pts1 = element1.getPtIndex(notUsedPts1);
                int pts2 = element1.getPtIndex((notUsedPts1 + 1) % 3);
                int pts3 = element2.getPtIndex(notUsedPts2);
                EfElement newElement1 = new EfElement(new int[] { pts1, pts2, pts3 });
                pts1 = element1.getPtIndex((notUsedPts1 + 2) % 3);
                pts2 = element1.getPtIndex(notUsedPts1);
                pts3 = element2.getPtIndex(notUsedPts2);
                EfElement newElement2 = new EfElement(new int[] { pts1, pts2, pts3 });
                elements[elementIdx[0]] = newElement1;
                elements[elementIdx[1]] = newElement2;
                modifiedElement.put(elementIdx[0], elementIdx[1]);
            }
            updater.majAvancement();
        }
        EfOperationResult res = new EfOperationResult();
        res.setGridData(new EfGridDataFromSwapSelectedEdge(dataGrid, new EfGridArray(grid.getNodes(), elements), modifiedElement));
        return res;
    }

    private boolean mustBeRefined(ProgressionInterface prog) {
        CtuluListSelection elementList = new CtuluListSelection();
        Set<EfSegment> newSelectedEdge = new HashSet<EfSegment>();
        EfNeighborMesh neighborMesh = EfNeighborMesh.compute(this.init.getGrid(), prog);
        TIntHashSet findedElement = new TIntHashSet();
        Iterator<EfSegment> iterator = this.selectedEdge.iterator();
        ProgressionUpdater updater = new ProgressionUpdater(prog);
        updater.majProgessionStateOnly("ef.swapSelectedEdge.mustBeRefine.msg");
        updater.setValue(10, this.selectedEdge.size());
        while (iterator.hasNext()) {
            if (this.stop) return false;
            EfSegment segment = iterator.next();
            int[] elementIdx = neighborMesh.getAdjacentMeshes(segment.getPt1Idx(), segment.getPt2Idx());
            if (elementIdx.length == 2) {
                newSelectedEdge.add(segment);
                for (int i = 0; i < elementIdx.length; i++) {
                    if (findedElement.contains(elementIdx[i])) {
                        elementList.add(elementIdx[i]);
                    } else {
                        findedElement.add(elementIdx[i]);
                    }
                }
            }
            updater.majAvancement();
        }
        this.selectedEdge = newSelectedEdge;
        this.elementToRefined = elementList;
        return !this.elementToRefined.isEmpty();
    }

    private int getNotUsedLocalPts(EfElement element, int pts1, int pts2) {
        for (int i = 0; i < element.getPtNb(); i++) {
            if ((element.getPtIndex(i) != pts1) && (element.getPtIndex(i) != pts2)) {
                return i;
            }
        }
        return -1;
    }

    public void setSelectedEdge(Set<EfSegment> selectedEdge) {
        this.selectedEdge = selectedEdge;
    }

    public void setInitGridData(EfGridData in) {
        this.init = in;
    }

    public void stop() {
        this.stop = true;
        if (this.operationRefine != null) {
            this.operationRefine.stop();
        }
    }
}
