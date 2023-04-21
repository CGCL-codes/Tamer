package ijgen.generator.engine.parser.java;

import ijgen.generator.annotations.Constraints;
import ijgen.generator.annotations.DBTable;
import ijgen.generator.annotations.GeneratedLayers;
import ijgen.generator.annotations.NonPersisted;
import ijgen.generator.annotations.PackageDesc;
import ijgen.generator.annotations.Persisted;
import ijgen.generator.annotations.Primary;
import ijgen.generator.annotations.Relation;
import ijgen.generator.annotations.UserExposed;
import ijgen.generator.engine.parser.ModelDefinitionsLoader;
import ijgen.generator.model.ClassDefinition;
import ijgen.generator.model.FieldConstraints;
import ijgen.generator.model.FieldDefinition;
import ijgen.generator.model.FieldJoinInformation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for scanning annotated java model files and loading meta information
 * into corresponding object structure used by generator. 
 *
 * @author Detelin Zlatev
 *
 */
public class JavaModelDefinitionsLoader extends ModelDefinitionsLoader {

    /**
	* Constant representing the java package keyword name.
	*/
    private static final String PACKAGE_IDENTIFIER = "package";

    /**
	 * {@inheritDoc}
	 */
    public JavaModelDefinitionsLoader(String modelDefinitionsDirectory) {
        super(modelDefinitionsDirectory);
    }

    /**
	 * {@inheritDoc}}
	 */
    @Override
    protected Map<String, ClassDefinition> doLoadClassDefinitions() {
        List<File> classDefinitionFiles = this.getModelFiles();
        Map<String, ClassDefinition> classDefinitions = new HashMap<String, ClassDefinition>();
        for (File classDefinitionFile : classDefinitionFiles) {
            ClassDefinition classDefinition = this.loadClassDefinition(classDefinitionFile);
            if (classDefinition != null) {
                classDefinitions.put(classDefinitionFile.getPath(), classDefinition);
            }
        }
        return classDefinitions;
    }

    /**
	 * Loads a class definition used by generator for the annotated java model file.
	 *
	 * @param file the file object representing annotated java model file
	 * 
	 * @return ClassDefinition
	 */
    private ClassDefinition loadClassDefinition(File file) {
        ClassDefinition classDefinition = null;
        String className = null;
        try {
            className = file.getName().substring(0, file.getName().indexOf("."));
            Class<?> clazz = Class.forName(this.determinePackageName(file) + "." + className);
            classDefinition = new ClassDefinition();
            classDefinition.setClassName(className);
            this.populateClassDefinition(classDefinition, clazz);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                FieldDefinition fieldDefinition = this.createFieldDefinition(field);
                classDefinition.addFieldDefinition(fieldDefinition);
            }
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Warning: a problem occurd during loading class: " + className);
            cnfe.printStackTrace();
        }
        return classDefinition;
    }

    /**
	* Creates and populates FieldDefinition object based on metadata contained in the passed field.
	*
	* @param field field information based on java reflection api
	* @return fieldDefinition used by generator based on the field informatin
	*/
    private FieldDefinition createFieldDefinition(Field field) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setFieldName(field.getName());
        fieldDefinition.setFieldType(field.getType().getName());
        Type genericFieldType = field.getGenericType();
        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            if (fieldArgTypes.length > 0) {
                Class<?> fieldArgClass = (Class<?>) fieldArgTypes[0];
                fieldDefinition.setGenericType(fieldArgClass.getName().substring(fieldArgClass.getName().lastIndexOf(".") + 1));
            }
        }
        Primary primary = (Primary) field.getAnnotation(Primary.class);
        if (primary != null) {
            fieldDefinition.setPrimary(true);
        }
        Persisted persisted = (Persisted) field.getAnnotation(Persisted.class);
        if (persisted != null) {
            fieldDefinition.setPersisted(true);
            fieldDefinition.setColumnName(persisted.columnName());
        }
        NonPersisted nonPersisted = (NonPersisted) field.getAnnotation(NonPersisted.class);
        if (nonPersisted != null) {
            fieldDefinition.setTransientField(true);
        }
        UserExposed userExposed = (UserExposed) field.getAnnotation(UserExposed.class);
        if (userExposed != null) {
            fieldDefinition.setUserExposed(true);
        }
        this.loadFieldConstraints(field, fieldDefinition);
        this.loadFieldJoinInformation(field, fieldDefinition);
        return fieldDefinition;
    }

    /**
	 * Loads declared field constraints and populates them into the object used by generator 
	 *
	 * @param field
	 * @param fieldDefinition
	 */
    private void loadFieldConstraints(Field field, FieldDefinition fieldDefinition) {
        Constraints constraints = (Constraints) field.getAnnotation(Constraints.class);
        if (constraints != null) {
            FieldConstraints fieldConstraints = new FieldConstraints();
            fieldConstraints.setRequired(constraints.required());
            fieldConstraints.setMinLength(constraints.minLength());
            fieldConstraints.setMaxLength(constraints.maxLength());
            fieldConstraints.setRegExpRule(constraints.regExpRule());
            fieldDefinition.setFieldConstraints(fieldConstraints);
        }
    }

    /**
	 * Loads declared field join information and populates it into the object used by generator 
	 * 
	 * @param field
	 * @param fieldDefinition
	 */
    private void loadFieldJoinInformation(Field field, FieldDefinition fieldDefinition) {
        Relation relation = (Relation) field.getAnnotation(Relation.class);
        if (relation != null) {
            FieldJoinInformation fieldJoinInformation = new FieldJoinInformation();
            fieldJoinInformation.setRelationType(relation.relationType());
            fieldJoinInformation.setJoinTable(relation.joinTable());
            fieldJoinInformation.setJoinColumn(relation.joinColumn());
            fieldDefinition.setFieldJoinInformation(fieldJoinInformation);
        }
    }

    /**
	* Loads class metadata using java reflection api into object used by generator
	*
	* @param classDefinition object used by the generator that is popupated
	* @param clazz object containing class info based on reflection
	*
	*/
    private void populateClassDefinition(ClassDefinition classDefinition, Class<?> clazz) {
        PackageDesc packageDesc = (PackageDesc) clazz.getAnnotation(PackageDesc.class);
        classDefinition.setPackageName(packageDesc.name());
        GeneratedLayers generatedLayers = (GeneratedLayers) clazz.getAnnotation(GeneratedLayers.class);
        if (generatedLayers != null) {
            if (generatedLayers.skipLayers() != null && generatedLayers.skipLayers().trim().length() > 0) {
                String[] layers = generatedLayers.skipLayers().split(",");
                List<String> skipLayersList = new ArrayList<String>();
                for (String layer : layers) {
                    skipLayersList.add(layer.trim());
                }
                classDefinition.setSkipLayers(skipLayersList);
            } else {
                classDefinition.setSkipLayers(new ArrayList<String>());
            }
            if (generatedLayers.layersFileNameSuffixes() != null && generatedLayers.layersFileNameSuffixes().trim().length() > 0) {
                String[] layersFileNameSuffixes = generatedLayers.layersFileNameSuffixes().split(",");
                Map<String, String> layersFileNameSuffixesList = new HashMap<String, String>();
                for (String layersFileNameSuffix : layersFileNameSuffixes) {
                    String[] entryParts = layersFileNameSuffix.split("=");
                    layersFileNameSuffixesList.put(entryParts[0].trim(), entryParts.length > 1 ? entryParts[1].trim() : "");
                }
                classDefinition.setGeneratedLayersFileNamesSuffixes(layersFileNameSuffixesList);
            } else {
                classDefinition.setGeneratedLayersFileNamesSuffixes(new HashMap<String, String>());
            }
        } else {
            classDefinition.setSkipLayers(new ArrayList<String>());
            classDefinition.setGeneratedLayersFileNamesSuffixes(new HashMap<String, String>());
        }
        DBTable dBTable = (DBTable) clazz.getAnnotation(DBTable.class);
        if (dBTable != null) {
            classDefinition.setDbTable(dBTable.name());
        }
    }

    /**
	* Gets the package name of java file pointed by the file descriptor parameter.
	*
	* @param file file resource containing the path to the java file
	*
	* @return package name
	*/
    private String determinePackageName(File file) {
        String packageName = null;
        if (file.isFile()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null && packageName == null) {
                    if (line.trim().startsWith(PACKAGE_IDENTIFIER)) {
                        packageName = line.substring(line.indexOf(PACKAGE_IDENTIFIER) + PACKAGE_IDENTIFIER.length(), line.trim().length() - 1).trim();
                    }
                }
                reader.close();
            } catch (FileNotFoundException fnfe) {
                System.out.println("File cannot be opened or does not exist!!!");
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                System.out.println("Error while opening file!!!");
                ioe.printStackTrace();
            }
        }
        return packageName;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected String getModelDefinitionExtension() {
        return "java";
    }
}
