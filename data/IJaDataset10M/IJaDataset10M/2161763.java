package cz.cuni.mff.ksi.jinfer.moduleselection;

import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.runner.MissingModuleException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openide.util.Lookup;

/**
 * Class providing methods for lookup modules important for inference.
 * @author sviro
 */
public final class ModuleSelection {

    public static final String MODULE_SELECTION_INITIAL_GRAMMAR = "moduleselector.initialgrammar";

    public static final String MODULE_SELECTION_SIMPIFIER = "moduleselector.simplifier";

    public static final String MODULE_SELECTION_SCHEMA_GENERATOR = "moduleselector.schemagenerator";

    private ModuleSelection() {
    }

    /**
   * 
   * @param name
   * @return
   */
    public static IGGenerator lookupIGGenerator(final String name) {
        final List<IGGenerator> igGenerators = lookupIGGenerators();
        if (igGenerators.isEmpty()) {
            throw new MissingModuleException("IG generator module not found.");
        }
        IGGenerator result = null;
        for (IGGenerator iGGenerator : igGenerators) {
            if (result == null) {
                result = iGGenerator;
            }
            if (iGGenerator.getModuleName().equals(name)) {
                return iGGenerator;
            }
        }
        return result;
    }

    /**
   * 
   * @param name
   * @return
   */
    public static Simplifier lookupSimplifier(final String name) {
        final List<Simplifier> simplifiers = lookupSimplifiers();
        if (simplifiers.isEmpty()) {
            throw new MissingModuleException("Simplifier module not found.");
        }
        Simplifier result = null;
        for (Simplifier simplifier : simplifiers) {
            if (result == null) {
                result = simplifier;
            }
            if (simplifier.getModuleName().equals(name)) {
                return simplifier;
            }
        }
        return result;
    }

    /**
   * 
   * @param name
   * @return
   */
    public static SchemaGenerator lookupSchemaGenerator(final String name) {
        final List<SchemaGenerator> schemaGenerators = lookupSchemaGenerators();
        if (schemaGenerators.isEmpty()) {
            throw new MissingModuleException("Schema generator module not found.");
        }
        SchemaGenerator result = null;
        for (SchemaGenerator schemaGenerator : schemaGenerators) {
            if (result == null) {
                result = schemaGenerator;
            }
            if (schemaGenerator.getModuleName().equals(name)) {
                return schemaGenerator;
            }
        }
        return result;
    }

    /**
   * 
   * @return
   */
    public static List<String> lookupIGGeneratorNames() {
        final List<String> list = new ArrayList<String>();
        for (IGGenerator igGenerator : lookupIGGenerators()) {
            list.add(igGenerator.getModuleName());
        }
        return list;
    }

    /**
   * 
   * @return
   */
    public static List<String> lookupSimplifierNames() {
        final List<String> list = new ArrayList<String>();
        for (Simplifier simplif : lookupSimplifiers()) {
            list.add(simplif.getModuleName());
        }
        return list;
    }

    /**
   * 
   * @return
   */
    public static List<String> lookupSchemaGeneratorNames() {
        final List<String> list = new ArrayList<String>();
        for (SchemaGenerator sg : lookupSchemaGenerators()) {
            list.add(sg.getModuleName());
        }
        return list;
    }

    private static List<Simplifier> lookupSimplifiers() {
        final Lookup lkp = Lookup.getDefault();
        final List<Simplifier> result = new ArrayList<Simplifier>(lkp.lookupAll(Simplifier.class));
        Collections.sort(result, new Comparator<Simplifier>() {

            @Override
            public int compare(final Simplifier o1, final Simplifier o2) {
                final String s1 = o1.getModuleName();
                final String s2 = o2.getModuleName();
                return s1.compareTo(s2);
            }
        });
        return result;
    }

    private static List<IGGenerator> lookupIGGenerators() {
        final Lookup lkp = Lookup.getDefault();
        final List<IGGenerator> result = new ArrayList<IGGenerator>(lkp.lookupAll(IGGenerator.class));
        Collections.sort(result, new Comparator<IGGenerator>() {

            @Override
            public int compare(final IGGenerator o1, final IGGenerator o2) {
                final String s1 = o1.getModuleName();
                final String s2 = o2.getModuleName();
                return s1.compareTo(s2);
            }
        });
        return result;
    }

    private static List<SchemaGenerator> lookupSchemaGenerators() {
        final Lookup lkp = Lookup.getDefault();
        final List<SchemaGenerator> result = new ArrayList<SchemaGenerator>(lkp.lookupAll(SchemaGenerator.class));
        Collections.sort(result, new Comparator<SchemaGenerator>() {

            @Override
            public int compare(final SchemaGenerator o1, final SchemaGenerator o2) {
                final String s1 = o1.getModuleName();
                final String s2 = o2.getModuleName();
                return s1.compareTo(s2);
            }
        });
        return result;
    }
}
