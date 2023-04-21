package proguard.obfuscate;

import proguard.classfile.*;
import proguard.classfile.visitor.*;
import java.io.IOException;
import java.util.*;

/**
 * This ClassFileVisitor obfuscates all class members in the name spaces of all
 * visited class file. The class members must have been linked before applying this
 * visitor. The class file is typically a class file that is not being subclassed.
 *
 * @see MemberInfoLinker
 *
 * @author Eric Lafortune
 */
public class MemberInfoObfuscator implements ClassFileVisitor {

    private static final char UNIQUE_SUFFIX = '_';

    private boolean allowAggressiveOverloading;

    private NameFactory nameFactory = new SimpleNameFactory();

    private NameFactory uniqueNameFactory = new SimpleNameFactory();

    private final Map nonPrivateDescriptorMap = new HashMap();

    private final Map privateDescriptorMap = new HashMap();

    /**
     * Creates a new MemberObfuscator.
     * @param allowAggressiveOverloading a flag that specifies whether class
     *                                   members can be overloaded aggressively.
     * @param obfuscationDictionary      the optional name of a file from which
     *                                   obfuscated method names can be read.
     */
    public MemberInfoObfuscator(boolean allowAggressiveOverloading, String obfuscationDictionary) throws IOException {
        this.allowAggressiveOverloading = allowAggressiveOverloading;
        if (obfuscationDictionary != null) {
            nameFactory = new ReadNameFactory(obfuscationDictionary, nameFactory);
        }
    }

    public void visitProgramClassFile(ProgramClassFile programClassFile) {
        programClassFile.hierarchyAccept(true, true, true, false, new AllMemberInfoVisitor(new MyNewNameCollector(nonPrivateDescriptorMap)));
        programClassFile.hierarchyAccept(true, true, true, false, new MyNonPrivateMemberInfoObfuscator());
        programClassFile.hierarchyAccept(true, true, true, false, new MyPrivateMemberInfoObfuscator());
        nonPrivateDescriptorMap.clear();
    }

    public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
    }

    private class MyNonPrivateMemberInfoObfuscator implements ClassFileVisitor {

        public void visitProgramClassFile(ProgramClassFile programClassFile) {
            MemberInfoAccessFilter nonPrivateNewNameAssigner = new MemberInfoAccessFilter(0, ClassConstants.INTERNAL_ACC_PRIVATE, new MyNewNameAssigner(nonPrivateDescriptorMap));
            programClassFile.fieldsAccept(nonPrivateNewNameAssigner);
            programClassFile.methodsAccept(nonPrivateNewNameAssigner);
        }

        public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
        }
    }

    private class MyPrivateMemberInfoObfuscator implements ClassFileVisitor {

        public void visitProgramClassFile(ProgramClassFile programClassFile) {
            MemberInfoAccessFilter privateNewNameAssigner = new MemberInfoAccessFilter(ClassConstants.INTERNAL_ACC_PRIVATE, 0, new MyNewNameAssigner(privateDescriptorMap, nonPrivateDescriptorMap));
            programClassFile.fieldsAccept(privateNewNameAssigner);
            programClassFile.methodsAccept(privateNewNameAssigner);
            privateDescriptorMap.clear();
        }

        public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
        }
    }

    private class MyNewNameCollector implements MemberInfoVisitor {

        private final Map descriptorMap;

        public MyNewNameCollector(Map descriptorMap) {
            this.descriptorMap = descriptorMap;
        }

        public void visitProgramFieldInfo(ProgramClassFile programClassFile, ProgramFieldInfo programFieldInfo) {
            visitMemberInfo(programClassFile, programFieldInfo);
        }

        public void visitProgramMethodInfo(ProgramClassFile programClassFile, ProgramMethodInfo programMethodInfo) {
            visitMemberInfo(programClassFile, programMethodInfo);
        }

        public void visitLibraryFieldInfo(LibraryClassFile libraryClassFile, LibraryFieldInfo libraryFieldInfo) {
            String name = libraryFieldInfo.getName(libraryClassFile);
            setNewMemberName(libraryFieldInfo, name);
            visitMemberInfo(libraryClassFile, libraryFieldInfo);
        }

        public void visitLibraryMethodInfo(LibraryClassFile libraryClassFile, LibraryMethodInfo libraryMethodInfo) {
            String name = libraryMethodInfo.getName(libraryClassFile);
            setNewMemberName(libraryMethodInfo, name);
            visitMemberInfo(libraryClassFile, libraryMethodInfo);
        }

        /**
         * Inserts the new name of the given class member into the main map.
         * @param classFile  the class file of the given member.
         * @param memberInfo the class member to be linked.
         */
        private void visitMemberInfo(ClassFile classFile, MemberInfo memberInfo) {
            String name = memberInfo.getName(classFile);
            if (name.equals(ClassConstants.INTERNAL_METHOD_NAME_CLINIT) || name.equals(ClassConstants.INTERNAL_METHOD_NAME_INIT)) {
                return;
            }
            String newName = newMemberName(memberInfo);
            if (newName != null) {
                String descriptor = memberInfo.getDescriptor(classFile);
                if (!allowAggressiveOverloading) {
                    descriptor = descriptor.substring(0, descriptor.indexOf(')') + 1);
                }
                Map newNameMap = retrieveNameMap(descriptorMap, descriptor);
                String otherName = (String) newNameMap.get(newName);
                if (otherName != null && !otherName.equals(name)) {
                    setNewMemberName(memberInfo, uniqueNameFactory.nextName() + UNIQUE_SUFFIX);
                } else {
                    newNameMap.put(newName, name);
                }
            }
        }
    }

    private class MyNewNameAssigner implements MemberInfoVisitor {

        private final Map descriptorMap;

        private final Map secondaryDescriptorMap;

        public MyNewNameAssigner(Map descriptorMap) {
            this(descriptorMap, null);
        }

        public MyNewNameAssigner(Map descriptorMap, Map secondaryDescriptorMap) {
            this.descriptorMap = descriptorMap;
            this.secondaryDescriptorMap = secondaryDescriptorMap;
        }

        public void visitProgramFieldInfo(ProgramClassFile programClassFile, ProgramFieldInfo programFieldInfo) {
            visitMemberInfo(programClassFile, programFieldInfo);
        }

        public void visitProgramMethodInfo(ProgramClassFile programClassFile, ProgramMethodInfo programMethodInfo) {
            visitMemberInfo(programClassFile, programMethodInfo);
        }

        public void visitLibraryFieldInfo(LibraryClassFile libraryClassFile, LibraryFieldInfo libraryFieldInfo) {
        }

        public void visitLibraryMethodInfo(LibraryClassFile libraryClassFile, LibraryMethodInfo libraryMethodInfo) {
        }

        /**
         * Inserts the given class member into the main map.
         * @param classFile  the class file of the given member.
         * @param memberInfo the class member to be linked.
         */
        private void visitMemberInfo(ClassFile classFile, MemberInfo memberInfo) {
            String name = memberInfo.getName(classFile);
            if (name.equals(ClassConstants.INTERNAL_METHOD_NAME_CLINIT) || name.equals(ClassConstants.INTERNAL_METHOD_NAME_INIT)) {
                return;
            }
            String newName = newMemberName(memberInfo);
            if (newName == null) {
                String descriptor = memberInfo.getDescriptor(classFile);
                if (!allowAggressiveOverloading) {
                    descriptor = descriptor.substring(0, descriptor.indexOf(')') + 1);
                }
                Map newNameMap = retrieveNameMap(descriptorMap, descriptor);
                Map secondaryNewNameMap = secondaryDescriptorMap == null ? null : (Map) secondaryDescriptorMap.get(descriptor);
                nameFactory.reset();
                do {
                    newName = nameFactory.nextName();
                } while (newNameMap.containsKey(newName) || (secondaryNewNameMap != null && secondaryNewNameMap.containsKey(newName)));
                setNewMemberName(memberInfo, newName);
                newNameMap.put(newName, name);
            }
        }
    }

    /**
     * Gets the nested set of new names, based on the given map
     * [descriptor - nested set of new names] and a given descriptor.
     * A new empty set is created if necessary.
     * @param descriptorMap the map of descriptors to [ new name - member info ] maps.
     * @param descriptor    the class member descriptor.
     * @return the nested set of new names.
     */
    private Map retrieveNameMap(Map descriptorMap, String descriptor) {
        Map nameMap = (Map) descriptorMap.get(descriptor);
        if (nameMap == null) {
            nameMap = new HashMap();
            descriptorMap.put(descriptor, nameMap);
        }
        return nameMap;
    }

    /**
     * Assigns a new name to the given class member.
     * @param memberInfo the given class member.
     * @param name       the new name.
     */
    static void setNewMemberName(MemberInfo memberInfo, String name) {
        MemberInfoLinker.lastMemberInfo(memberInfo).setVisitorInfo(name);
    }

    /**
     * Retrieves the new name of the given class member.
     * @param memberInfo the given class member.
     * @return the class member's new name, or <code>null</code> if it doesn't
     *         have one yet.
     */
    static String newMemberName(MemberInfo memberInfo) {
        return (String) MemberInfoLinker.lastMemberInfo(memberInfo).getVisitorInfo();
    }
}
