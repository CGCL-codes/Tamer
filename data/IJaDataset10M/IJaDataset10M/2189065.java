package org.eclipse.jdt.internal.core.builder;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.AccessRuleSet;
import org.eclipse.jdt.internal.compiler.env.AccessRule;
import org.eclipse.jdt.internal.compiler.util.SimpleLookupTable;
import org.eclipse.jdt.internal.core.ClasspathAccessRule;
import org.eclipse.jdt.internal.core.JavaModelManager;
import java.io.*;
import java.util.*;

public class State {

    String javaProjectName;

    ClasspathMultiDirectory[] sourceLocations;

    ClasspathLocation[] binaryLocations;

    SimpleLookupTable references;

    public SimpleLookupTable typeLocators;

    int buildNumber;

    long lastStructuralBuildTime;

    SimpleLookupTable structuralBuildTimes;

    private String[] knownPackageNames;

    private long previousStructuralBuildTime;

    private StringSet structurallyChangedTypes;

    public static int MaxStructurallyChangedTypes = 100;

    public static final byte VERSION = 0x0016;

    static final byte SOURCE_FOLDER = 1;

    static final byte BINARY_FOLDER = 2;

    static final byte EXTERNAL_JAR = 3;

    static final byte INTERNAL_JAR = 4;

    State() {
    }

    protected State(JavaBuilder javaBuilder) {
        this.knownPackageNames = null;
        this.previousStructuralBuildTime = -1;
        this.structurallyChangedTypes = null;
        this.javaProjectName = javaBuilder.currentProject.getName();
        this.sourceLocations = javaBuilder.nameEnvironment.sourceLocations;
        this.binaryLocations = javaBuilder.nameEnvironment.binaryLocations;
        this.references = new SimpleLookupTable(7);
        this.typeLocators = new SimpleLookupTable(7);
        this.buildNumber = 0;
        this.lastStructuralBuildTime = computeStructuralBuildTime(javaBuilder.lastState == null ? 0 : javaBuilder.lastState.lastStructuralBuildTime);
        this.structuralBuildTimes = new SimpleLookupTable(3);
    }

    long computeStructuralBuildTime(long previousTime) {
        long newTime = System.currentTimeMillis();
        if (newTime <= previousTime) newTime = previousTime + 1;
        return newTime;
    }

    void copyFrom(State lastState) {
        this.knownPackageNames = null;
        this.previousStructuralBuildTime = lastState.previousStructuralBuildTime;
        this.structurallyChangedTypes = lastState.structurallyChangedTypes;
        this.buildNumber = lastState.buildNumber + 1;
        this.lastStructuralBuildTime = lastState.lastStructuralBuildTime;
        this.structuralBuildTimes = lastState.structuralBuildTimes;
        try {
            this.references = (SimpleLookupTable) lastState.references.clone();
            this.typeLocators = (SimpleLookupTable) lastState.typeLocators.clone();
        } catch (CloneNotSupportedException e) {
            this.references = new SimpleLookupTable(lastState.references.elementSize);
            Object[] keyTable = lastState.references.keyTable;
            Object[] valueTable = lastState.references.valueTable;
            for (int i = 0, l = keyTable.length; i < l; i++) if (keyTable[i] != null) this.references.put(keyTable[i], valueTable[i]);
            this.typeLocators = new SimpleLookupTable(lastState.typeLocators.elementSize);
            keyTable = lastState.typeLocators.keyTable;
            valueTable = lastState.typeLocators.valueTable;
            for (int i = 0, l = keyTable.length; i < l; i++) if (keyTable[i] != null) this.typeLocators.put(keyTable[i], valueTable[i]);
        }
    }

    public char[][] getDefinedTypeNamesFor(String typeLocator) {
        Object c = references.get(typeLocator);
        if (c instanceof AdditionalTypeCollection) return ((AdditionalTypeCollection) c).definedTypeNames;
        return null;
    }

    public SimpleLookupTable getReferences() {
        return this.references;
    }

    StringSet getStructurallyChangedTypes(State prereqState) {
        if (prereqState != null && prereqState.previousStructuralBuildTime > 0) {
            Object o = structuralBuildTimes.get(prereqState.javaProjectName);
            long previous = o == null ? 0 : ((Long) o).longValue();
            if (previous == prereqState.previousStructuralBuildTime) return prereqState.structurallyChangedTypes;
        }
        return null;
    }

    public boolean isDuplicateLocator(String qualifiedTypeName, String typeLocator) {
        String existing = (String) typeLocators.get(qualifiedTypeName);
        return existing != null && !existing.equals(typeLocator);
    }

    public boolean isKnownPackage(String qualifiedPackageName) {
        if (knownPackageNames == null) {
            ArrayList names = new ArrayList(typeLocators.elementSize);
            Object[] keyTable = typeLocators.keyTable;
            for (int i = 0, l = keyTable.length; i < l; i++) {
                if (keyTable[i] != null) {
                    String packageName = (String) keyTable[i];
                    int last = packageName.lastIndexOf('/');
                    packageName = last == -1 ? null : packageName.substring(0, last);
                    while (packageName != null && !names.contains(packageName)) {
                        names.add(packageName);
                        last = packageName.lastIndexOf('/');
                        packageName = last == -1 ? null : packageName.substring(0, last);
                    }
                }
            }
            knownPackageNames = new String[names.size()];
            names.toArray(knownPackageNames);
        }
        for (int i = 0, l = knownPackageNames.length; i < l; i++) if (knownPackageNames[i].equals(qualifiedPackageName)) return true;
        return false;
    }

    public boolean isKnownType(String qualifiedTypeName) {
        return typeLocators.containsKey(qualifiedTypeName);
    }

    void record(String typeLocator, char[][][] qualifiedRefs, char[][] simpleRefs, char[] mainTypeName, ArrayList typeNames) {
        if (typeNames.size() == 1 && CharOperation.equals(mainTypeName, (char[]) typeNames.get(0))) {
            references.put(typeLocator, new ReferenceCollection(qualifiedRefs, simpleRefs));
        } else {
            char[][] definedTypeNames = new char[typeNames.size()][];
            typeNames.toArray(definedTypeNames);
            references.put(typeLocator, new AdditionalTypeCollection(definedTypeNames, qualifiedRefs, simpleRefs));
        }
    }

    void recordLocatorForType(String qualifiedTypeName, String typeLocator) {
        this.knownPackageNames = null;
        int start = typeLocator.indexOf(qualifiedTypeName, 0);
        if (start > 0) qualifiedTypeName = typeLocator.substring(start, start + qualifiedTypeName.length());
        typeLocators.put(qualifiedTypeName, typeLocator);
    }

    void recordStructuralDependency(IProject prereqProject, State prereqState) {
        if (prereqState != null) if (prereqState.lastStructuralBuildTime > 0) structuralBuildTimes.put(prereqProject.getName(), new Long(prereqState.lastStructuralBuildTime));
    }

    void removeLocator(String typeLocatorToRemove) {
        this.knownPackageNames = null;
        references.removeKey(typeLocatorToRemove);
        typeLocators.removeValue(typeLocatorToRemove);
    }

    void removePackage(IResourceDelta sourceDelta) {
        IResource resource = sourceDelta.getResource();
        switch(resource.getType()) {
            case IResource.FOLDER:
                IResourceDelta[] children = sourceDelta.getAffectedChildren();
                for (int i = 0, l = children.length; i < l; i++) removePackage(children[i]);
                return;
            case IResource.FILE:
                IPath typeLocatorPath = resource.getProjectRelativePath();
                if (org.eclipse.jdt.internal.core.util.Util.isJavaLikeFileName(typeLocatorPath.lastSegment())) removeLocator(typeLocatorPath.toString());
        }
    }

    void removeQualifiedTypeName(String qualifiedTypeNameToRemove) {
        this.knownPackageNames = null;
        typeLocators.removeKey(qualifiedTypeNameToRemove);
    }

    static State read(IProject project, DataInputStream in) throws IOException {
        if (JavaBuilder.DEBUG) System.out.println("About to read state " + project.getName());
        if (VERSION != in.readByte()) {
            if (JavaBuilder.DEBUG) System.out.println("Found non-compatible state version... answered null for " + project.getName());
            return null;
        }
        State newState = new State();
        newState.javaProjectName = in.readUTF();
        if (!project.getName().equals(newState.javaProjectName)) {
            if (JavaBuilder.DEBUG) System.out.println("Project's name does not match... answered null");
            return null;
        }
        newState.buildNumber = in.readInt();
        newState.lastStructuralBuildTime = in.readLong();
        int length = in.readInt();
        newState.sourceLocations = new ClasspathMultiDirectory[length];
        for (int i = 0; i < length; i++) {
            IContainer sourceFolder = project, outputFolder = project;
            String folderName;
            if ((folderName = in.readUTF()).length() > 0) sourceFolder = project.getFolder(folderName);
            if ((folderName = in.readUTF()).length() > 0) outputFolder = project.getFolder(folderName);
            ClasspathMultiDirectory md = (ClasspathMultiDirectory) ClasspathLocation.forSourceFolder(sourceFolder, outputFolder, readNames(in), readNames(in));
            if (in.readBoolean()) md.hasIndependentOutputFolder = true;
            newState.sourceLocations[i] = md;
        }
        length = in.readInt();
        newState.binaryLocations = new ClasspathLocation[length];
        IWorkspaceRoot root = project.getWorkspace().getRoot();
        for (int i = 0; i < length; i++) {
            switch(in.readByte()) {
                case SOURCE_FOLDER:
                    newState.binaryLocations[i] = newState.sourceLocations[in.readInt()];
                    break;
                case BINARY_FOLDER:
                    IPath path = new Path(in.readUTF());
                    IContainer outputFolder = path.segmentCount() == 1 ? (IContainer) root.getProject(path.toString()) : (IContainer) root.getFolder(path);
                    newState.binaryLocations[i] = ClasspathLocation.forBinaryFolder(outputFolder, in.readBoolean(), readRestriction(in));
                    break;
                case EXTERNAL_JAR:
                    newState.binaryLocations[i] = ClasspathLocation.forLibrary(in.readUTF(), in.readLong(), readRestriction(in));
                    break;
                case INTERNAL_JAR:
                    newState.binaryLocations[i] = ClasspathLocation.forLibrary(root.getFile(new Path(in.readUTF())), readRestriction(in));
            }
        }
        newState.structuralBuildTimes = new SimpleLookupTable(length = in.readInt());
        for (int i = 0; i < length; i++) newState.structuralBuildTimes.put(in.readUTF(), new Long(in.readLong()));
        String[] internedTypeLocators = new String[length = in.readInt()];
        for (int i = 0; i < length; i++) internedTypeLocators[i] = in.readUTF();
        newState.typeLocators = new SimpleLookupTable(length = in.readInt());
        for (int i = 0; i < length; i++) newState.recordLocatorForType(in.readUTF(), internedTypeLocators[in.readInt()]);
        char[][] internedSimpleNames = ReferenceCollection.internSimpleNames(readNames(in), false);
        char[][][] internedQualifiedNames = new char[length = in.readInt()][][];
        for (int i = 0; i < length; i++) {
            int qLength = in.readInt();
            char[][] qName = new char[qLength][];
            for (int j = 0; j < qLength; j++) qName[j] = internedSimpleNames[in.readInt()];
            internedQualifiedNames[i] = qName;
        }
        internedQualifiedNames = ReferenceCollection.internQualifiedNames(internedQualifiedNames);
        newState.references = new SimpleLookupTable(length = in.readInt());
        for (int i = 0; i < length; i++) {
            String typeLocator = internedTypeLocators[in.readInt()];
            ReferenceCollection collection = null;
            switch(in.readByte()) {
                case 1:
                    char[][] additionalTypeNames = readNames(in);
                    char[][][] qualifiedNames = new char[in.readInt()][][];
                    for (int j = 0, m = qualifiedNames.length; j < m; j++) qualifiedNames[j] = internedQualifiedNames[in.readInt()];
                    char[][] simpleNames = new char[in.readInt()][];
                    for (int j = 0, m = simpleNames.length; j < m; j++) simpleNames[j] = internedSimpleNames[in.readInt()];
                    collection = new AdditionalTypeCollection(additionalTypeNames, qualifiedNames, simpleNames);
                    break;
                case 2:
                    char[][][] qNames = new char[in.readInt()][][];
                    for (int j = 0, m = qNames.length; j < m; j++) qNames[j] = internedQualifiedNames[in.readInt()];
                    char[][] sNames = new char[in.readInt()][];
                    for (int j = 0, m = sNames.length; j < m; j++) sNames[j] = internedSimpleNames[in.readInt()];
                    collection = new ReferenceCollection(qNames, sNames);
            }
            newState.references.put(typeLocator, collection);
        }
        if (JavaBuilder.DEBUG) System.out.println("Successfully read state for " + newState.javaProjectName);
        return newState;
    }

    private static char[] readName(DataInputStream in) throws IOException {
        int nLength = in.readInt();
        char[] name = new char[nLength];
        for (int j = 0; j < nLength; j++) name[j] = in.readChar();
        return name;
    }

    private static char[][] readNames(DataInputStream in) throws IOException {
        int length = in.readInt();
        char[][] names = new char[length][];
        for (int i = 0; i < length; i++) names[i] = readName(in);
        return names;
    }

    private static AccessRuleSet readRestriction(DataInputStream in) throws IOException {
        int length = in.readInt();
        if (length == 0) return null;
        AccessRule[] accessRules = new AccessRule[length];
        for (int i = 0; i < length; i++) {
            char[] pattern = readName(in);
            int problemId = in.readInt();
            accessRules[i] = new ClasspathAccessRule(pattern, problemId);
        }
        JavaModelManager manager = JavaModelManager.getJavaModelManager();
        return new AccessRuleSet(accessRules, in.readByte(), manager.intern(in.readUTF()));
    }

    void tagAsNoopBuild() {
        this.buildNumber = -1;
    }

    boolean wasNoopBuild() {
        return buildNumber == -1;
    }

    void tagAsStructurallyChanged() {
        this.previousStructuralBuildTime = this.lastStructuralBuildTime;
        this.structurallyChangedTypes = new StringSet(7);
        this.lastStructuralBuildTime = computeStructuralBuildTime(this.previousStructuralBuildTime);
    }

    boolean wasStructurallyChanged(IProject prereqProject, State prereqState) {
        if (prereqState != null) {
            Object o = structuralBuildTimes.get(prereqProject.getName());
            long previous = o == null ? 0 : ((Long) o).longValue();
            if (previous == prereqState.lastStructuralBuildTime) return false;
        }
        return true;
    }

    void wasStructurallyChanged(String typeName) {
        if (this.structurallyChangedTypes != null) {
            if (this.structurallyChangedTypes.elementSize > MaxStructurallyChangedTypes) this.structurallyChangedTypes = null; else this.structurallyChangedTypes.add(typeName);
        }
    }

    void write(DataOutputStream out) throws IOException {
        int length;
        Object[] keyTable;
        Object[] valueTable;
        out.writeByte(VERSION);
        out.writeUTF(javaProjectName);
        out.writeInt(buildNumber);
        out.writeLong(lastStructuralBuildTime);
        out.writeInt(length = sourceLocations.length);
        for (int i = 0; i < length; i++) {
            ClasspathMultiDirectory md = sourceLocations[i];
            out.writeUTF(md.sourceFolder.getProjectRelativePath().toString());
            out.writeUTF(md.binaryFolder.getProjectRelativePath().toString());
            writeNames(md.inclusionPatterns, out);
            writeNames(md.exclusionPatterns, out);
            out.writeBoolean(md.hasIndependentOutputFolder);
        }
        out.writeInt(length = binaryLocations.length);
        next: for (int i = 0; i < length; i++) {
            ClasspathLocation c = binaryLocations[i];
            if (c instanceof ClasspathMultiDirectory) {
                out.writeByte(SOURCE_FOLDER);
                for (int j = 0, m = sourceLocations.length; j < m; j++) {
                    if (sourceLocations[j] == c) {
                        out.writeInt(j);
                        continue next;
                    }
                }
            } else if (c instanceof ClasspathDirectory) {
                out.writeByte(BINARY_FOLDER);
                ClasspathDirectory cd = (ClasspathDirectory) c;
                out.writeUTF(cd.binaryFolder.getFullPath().toString());
                out.writeBoolean(cd.isOutputFolder);
                writeRestriction(cd.accessRuleSet, out);
            } else {
                ClasspathJar jar = (ClasspathJar) c;
                if (jar.resource == null) {
                    out.writeByte(EXTERNAL_JAR);
                    out.writeUTF(jar.zipFilename);
                    out.writeLong(jar.lastModified());
                } else {
                    out.writeByte(INTERNAL_JAR);
                    out.writeUTF(jar.resource.getFullPath().toString());
                }
                writeRestriction(jar.accessRuleSet, out);
            }
        }
        out.writeInt(length = structuralBuildTimes.elementSize);
        if (length > 0) {
            keyTable = structuralBuildTimes.keyTable;
            valueTable = structuralBuildTimes.valueTable;
            for (int i = 0, l = keyTable.length; i < l; i++) {
                if (keyTable[i] != null) {
                    length--;
                    out.writeUTF((String) keyTable[i]);
                    out.writeLong(((Long) valueTable[i]).longValue());
                }
            }
            if (JavaBuilder.DEBUG && length != 0) System.out.println("structuralBuildNumbers table is inconsistent");
        }
        out.writeInt(length = references.elementSize);
        SimpleLookupTable internedTypeLocators = new SimpleLookupTable(length);
        if (length > 0) {
            keyTable = references.keyTable;
            for (int i = 0, l = keyTable.length; i < l; i++) {
                if (keyTable[i] != null) {
                    length--;
                    String key = (String) keyTable[i];
                    out.writeUTF(key);
                    internedTypeLocators.put(key, new Integer(internedTypeLocators.elementSize));
                }
            }
            if (JavaBuilder.DEBUG && length != 0) System.out.println("references table is inconsistent");
        }
        out.writeInt(length = typeLocators.elementSize);
        if (length > 0) {
            keyTable = typeLocators.keyTable;
            valueTable = typeLocators.valueTable;
            for (int i = 0, l = keyTable.length; i < l; i++) {
                if (keyTable[i] != null) {
                    length--;
                    out.writeUTF((String) keyTable[i]);
                    Integer index = (Integer) internedTypeLocators.get(valueTable[i]);
                    out.writeInt(index.intValue());
                }
            }
            if (JavaBuilder.DEBUG && length != 0) System.out.println("typeLocators table is inconsistent");
        }
        SimpleLookupTable internedQualifiedNames = new SimpleLookupTable(31);
        SimpleLookupTable internedSimpleNames = new SimpleLookupTable(31);
        valueTable = references.valueTable;
        for (int i = 0, l = valueTable.length; i < l; i++) {
            if (valueTable[i] != null) {
                ReferenceCollection collection = (ReferenceCollection) valueTable[i];
                char[][][] qNames = collection.qualifiedNameReferences;
                for (int j = 0, m = qNames.length; j < m; j++) {
                    char[][] qName = qNames[j];
                    if (!internedQualifiedNames.containsKey(qName)) {
                        internedQualifiedNames.put(qName, new Integer(internedQualifiedNames.elementSize));
                        for (int k = 0, n = qName.length; k < n; k++) {
                            char[] sName = qName[k];
                            if (!internedSimpleNames.containsKey(sName)) internedSimpleNames.put(sName, new Integer(internedSimpleNames.elementSize));
                        }
                    }
                }
                char[][] sNames = collection.simpleNameReferences;
                for (int j = 0, m = sNames.length; j < m; j++) {
                    char[] sName = sNames[j];
                    if (!internedSimpleNames.containsKey(sName)) internedSimpleNames.put(sName, new Integer(internedSimpleNames.elementSize));
                }
            }
        }
        char[][] internedArray = new char[internedSimpleNames.elementSize][];
        Object[] simpleNames = internedSimpleNames.keyTable;
        Object[] positions = internedSimpleNames.valueTable;
        for (int i = positions.length; --i >= 0; ) {
            if (positions[i] != null) {
                int index = ((Integer) positions[i]).intValue();
                internedArray[index] = (char[]) simpleNames[i];
            }
        }
        writeNames(internedArray, out);
        char[][][] internedQArray = new char[internedQualifiedNames.elementSize][][];
        Object[] qualifiedNames = internedQualifiedNames.keyTable;
        positions = internedQualifiedNames.valueTable;
        for (int i = positions.length; --i >= 0; ) {
            if (positions[i] != null) {
                int index = ((Integer) positions[i]).intValue();
                internedQArray[index] = (char[][]) qualifiedNames[i];
            }
        }
        out.writeInt(length = internedQArray.length);
        for (int i = 0; i < length; i++) {
            char[][] qName = internedQArray[i];
            int qLength = qName.length;
            out.writeInt(qLength);
            for (int j = 0; j < qLength; j++) {
                Integer index = (Integer) internedSimpleNames.get(qName[j]);
                out.writeInt(index.intValue());
            }
        }
        out.writeInt(length = references.elementSize);
        if (length > 0) {
            keyTable = references.keyTable;
            for (int i = 0, l = keyTable.length; i < l; i++) {
                if (keyTable[i] != null) {
                    length--;
                    Integer index = (Integer) internedTypeLocators.get(keyTable[i]);
                    out.writeInt(index.intValue());
                    ReferenceCollection collection = (ReferenceCollection) valueTable[i];
                    if (collection instanceof AdditionalTypeCollection) {
                        out.writeByte(1);
                        AdditionalTypeCollection atc = (AdditionalTypeCollection) collection;
                        writeNames(atc.definedTypeNames, out);
                    } else {
                        out.writeByte(2);
                    }
                    char[][][] qNames = collection.qualifiedNameReferences;
                    int qLength = qNames.length;
                    out.writeInt(qLength);
                    for (int j = 0; j < qLength; j++) {
                        index = (Integer) internedQualifiedNames.get(qNames[j]);
                        out.writeInt(index.intValue());
                    }
                    char[][] sNames = collection.simpleNameReferences;
                    int sLength = sNames.length;
                    out.writeInt(sLength);
                    for (int j = 0; j < sLength; j++) {
                        index = (Integer) internedSimpleNames.get(sNames[j]);
                        out.writeInt(index.intValue());
                    }
                }
            }
            if (JavaBuilder.DEBUG && length != 0) System.out.println("references table is inconsistent");
        }
    }

    private void writeName(char[] name, DataOutputStream out) throws IOException {
        int nLength = name.length;
        out.writeInt(nLength);
        for (int j = 0; j < nLength; j++) out.writeChar(name[j]);
    }

    private void writeNames(char[][] names, DataOutputStream out) throws IOException {
        int length = names == null ? 0 : names.length;
        out.writeInt(length);
        for (int i = 0; i < length; i++) writeName(names[i], out);
    }

    private void writeRestriction(AccessRuleSet accessRuleSet, DataOutputStream out) throws IOException {
        if (accessRuleSet == null) {
            out.writeInt(0);
        } else {
            AccessRule[] accessRules = accessRuleSet.getAccessRules();
            int length = accessRules.length;
            out.writeInt(length);
            if (length != 0) {
                for (int i = 0; i < length; i++) {
                    AccessRule accessRule = accessRules[i];
                    writeName(accessRule.pattern, out);
                    out.writeInt(accessRule.problemId);
                }
                out.writeByte(accessRuleSet.classpathEntryType);
                out.writeUTF(accessRuleSet.classpathEntryName);
            }
        }
    }

    /**
 * Returns a string representation of the receiver.
 */
    public String toString() {
        return "State for " + javaProjectName + " (#" + buildNumber + " @ " + new Date(lastStructuralBuildTime) + ")";
    }
}
