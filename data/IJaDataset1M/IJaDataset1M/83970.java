package com.dustedpixels.jasmin.unit.compiler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class ProcessCompilerImpl implements ProcessCompiler {

    private String[] sourcePaths;

    private UnitLoader unitLoader;

    private UnitCompiler unitWelder;

    private ProcessCodegen processCodegen;

    private ProcessCodeWriter processCodeWriter;

    private Map<String, Unit> parsedUnitsMap;

    private String destProcessName;

    public ProcessCompilerImpl(UnitLoader unitLoader, UnitCompiler unitWelder, ProcessCodegen processCodegen) {
        this.unitLoader = unitLoader;
        this.unitWelder = unitWelder;
        this.processCodegen = processCodegen;
    }

    public void compile(String sourceUnitName, String destProcessName, String[] sourcePath) throws Exception {
        this.destProcessName = destProcessName;
        this.sourcePaths = sourcePath;
        this.parsedUnitsMap = new HashMap<String, Unit>();
        Unit unit = parseAndWeld(sourceUnitName);
        String code = processCodegen.generateCode(unit);
        processCodeWriter.write(destProcessName, code);
    }

    private Unit parseByUnitName(String qualifiedUnitName) throws Exception {
        Unit unit = parsedUnitsMap.get(qualifiedUnitName);
        if (unit == null) {
            String unitPath = findSourceUnitPath(qualifiedUnitName);
            String unitCode = readSourceUnitCode(unitPath);
            unit = unitParser.parse(unitCode);
            parsedUnitsMap.put(qualifiedUnitName, unit);
        }
        return unit;
    }

    private Unit parseAndWeld(String sourceUnitName) throws Exception {
        Unit unit = parseByUnitName(sourceUnitName);
        Map<String, Unit> weldedUnits = new HashMap<String, Unit>();
        for (UnitReference unitReference : unit.unitReferences) {
            Unit weldedUnit = parseAndWeld(unitReference.qualifiedUnitName);
            weldedUnits.put(unitReference.referenceName, weldedUnit);
        }
        return unitWelder.compile(unit, sourceUnitName, weldedUnits);
    }
}
