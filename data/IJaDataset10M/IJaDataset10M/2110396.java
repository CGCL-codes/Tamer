package no.ugland.utransprod.util.report;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import no.ugland.utransprod.model.Assembly;
import no.ugland.utransprod.model.Supplier;
import no.ugland.utransprod.util.Util;
import no.ugland.utransprod.util.YearWeek;

public class AssemblyWeekReport {

    private YearWeek currentYearWeek;

    private YearWeek firstYearWeek;

    private YearWeek lastYearWeek;

    private Supplier supplier;

    public AssemblyWeekReport(final YearWeek yearWeek, final Supplier aSupplier) {
        currentYearWeek = yearWeek;
        firstYearWeek = Util.addWeek(currentYearWeek, -1);
        lastYearWeek = Util.addWeek(currentYearWeek, 1);
        supplier = aSupplier;
    }

    public String getSupplierName() {
        return supplier.getSupplierName();
    }

    public List<Assembly> getAssembliesWeek1() {
        return getAssembliesInWeek(firstYearWeek);
    }

    public List<Assembly> getAssembliesWeek2() {
        return getAssembliesInWeek(currentYearWeek);
    }

    public List<Assembly> getAssembliesWeek3() {
        return getAssembliesInWeek(lastYearWeek);
    }

    public List<Assembly> getAssembliesInWeek(YearWeek yearWeek) {
        Set<Assembly> assemblies = supplier.getAssemblies() != null ? supplier.getAssemblies() : new HashSet<Assembly>();
        List<Assembly> assemblyList = new ArrayList<Assembly>();
        if (assemblies != null) {
            for (Assembly assembly : assemblies) {
                boolean added = assembly.isForWeek(yearWeek.getYear(), yearWeek.getWeek()) ? assemblyList.add(assembly) : false;
            }
        }
        return assemblyList;
    }
}
