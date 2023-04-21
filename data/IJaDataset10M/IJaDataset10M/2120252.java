package gr.aueb.cs.nlg.NLFiles;

import java.io.*;

public class ClassInstanceKey {

    public String ClassURI;

    public String forInstance;

    public ClassInstanceKey(String ClassURI, String forInstance) {
        this.ClassURI = ClassURI;
        this.forInstance = forInstance;
    }

    public void print() {
        System.out.println("=ClassInstanceKey=");
        System.out.println("ClassURI:" + ClassURI);
        System.out.println("forInstance:" + forInstance);
    }

    public int hashCode() {
        return ClassURI.hashCode() + forInstance.hashCode();
    }

    public boolean equals(Object o) {
        ClassInstanceKey temp = (ClassInstanceKey) o;
        if (temp.ClassURI.compareTo(ClassURI) == 0 && temp.forInstance.compareTo(forInstance) == 0) return true; else return false;
    }
}
