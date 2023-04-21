package com.thoughtworks.bytecode2class.attribute;

import com.thoughtworks.bytecode2class.AccessFlag;
import com.thoughtworks.bytecode2class.ClassReader;
import java.util.ArrayList;
import java.util.List;

public class InnerClassesAttribute extends Attribute {

    private int numberOfClasses;

    private List<InnerClass> classes;

    public InnerClassesAttribute(ClassReader classReader, String name) {
        super(classReader, name);
    }

    public List<InnerClass> getClasses() {
        return classes;
    }

    protected void parseAttributeContent() {
        parseNumberOfClasses();
        parseClasses();
    }

    private void parseClasses() {
        for (int i = 0; i < numberOfClasses; i++) {
            classes.add(new InnerClass(classReader));
        }
    }

    private void parseNumberOfClasses() {
        numberOfClasses = classReader.forwardChar();
        classes = new ArrayList<InnerClass>(numberOfClasses);
    }

    public List<InnerClass> getInnerClasses() {
        return classes;
    }

    public class InnerClass {

        private ClassReader classReader;

        private int innerClassInfoIndex;

        private int outerClassInfoIndex;

        private int innerNameIndex;

        private List<AccessFlag> accessFlags;

        public InnerClass(ClassReader classReader) {
            this.classReader = classReader;
            parseInnerClassInfoIndex();
            parseOuterClassInfoIndex();
            parseInnerNameIndex();
            parseInnerClassAccessFlags();
        }

        private void parseInnerClassInfoIndex() {
            innerClassInfoIndex = classReader.forwardChar();
        }

        private void parseOuterClassInfoIndex() {
            outerClassInfoIndex = classReader.forwardChar();
        }

        private void parseInnerNameIndex() {
            innerNameIndex = classReader.forwardChar();
        }

        private void parseInnerClassAccessFlags() {
            int accessFlagsInt = classReader.forwardChar();
            this.accessFlags = classReader.calculateAccessFlags(accessFlagsInt);
        }

        public String getInnerClass() {
            return getClassName(innerClassInfoIndex);
        }

        public String getOuterClass() {
            return getClassName(outerClassInfoIndex);
        }

        public List<AccessFlag> getAccessFlags() {
            return accessFlags;
        }

        public String getName() {
            return classReader.getConstantPool().getUtf8Constant(innerNameIndex).getValue();
        }

        private String getClassName(int classInfoIndex) {
            if (classInfoIndex == 0) {
                return "Class Index is zero.";
            }
            return classReader.getConstantPool().getClassConstant(classInfoIndex).getName();
        }
    }
}
