package br.com.jsigner.diagram;

import java.util.ArrayList;
import java.util.List;
import br.com.jsigner.diagram.elements.Clazz;
import br.com.jsigner.interpreter.ClassDiagramVisitor;

public class ClassDiagram {

    private String name;

    private List<Clazz> classes = new ArrayList<Clazz>();

    private List<String> classesNames = new ArrayList<String>();

    public ClassDiagram(String diagramName, List<Class<?>> classes) {
        this.name = diagramName;
        for (Class<?> class1 : classes) {
            this.addClass(class1);
        }
    }

    public void addClass(Class<?> clazz) {
        this.classes.add(new Clazz(clazz, this));
        this.classesNames.add(clazz.getName());
    }

    public String getName() {
        return name;
    }

    public boolean containsClass(Class<?> clazz) {
        for (Clazz existingClazz : classes) {
            if (existingClazz.wrappedClassEquals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getClassesNames() {
        return this.classesNames;
    }

    public void accept(ClassDiagramVisitor visitor) {
        for (Clazz clazz : classes) {
            clazz.setup();
        }
        visitor.setup(classes);
        visitor.visit(this);
    }
}
