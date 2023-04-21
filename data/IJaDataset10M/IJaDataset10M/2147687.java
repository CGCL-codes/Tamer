package it.freax.fpm.core.solver.dto;

import it.freax.fpm.core.executor.Instruction;
import it.freax.fpm.core.solver.specs.DummySpec;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;

public class CompilationUnit {

    private String Name;

    private DummySpec dummySpec;

    private String PrincipalLang;

    private HashSet<String> Langs;

    private ArrayList<SrcFile> ContainedFiles;

    private LinkedList<Instruction> InstructionSet;

    public CompilationUnit(String Name) {
        this.Name = Name;
        dummySpec = new DummySpec();
        Langs = new HashSet<String>();
        ContainedFiles = new ArrayList<SrcFile>();
        InstructionSet = new LinkedList<Instruction>();
    }

    public String getName() {
        return Name;
    }

    public void setName(String value) {
        Name = value;
    }

    public DummySpec getDummySpec() {
        return dummySpec;
    }

    public void setDummySpec(DummySpec dummySpec) {
        this.dummySpec = dummySpec;
    }

    public String getPrincipalLang() {
        return PrincipalLang;
    }

    public void setPrincipalLang(String value) {
        PrincipalLang = value;
    }

    public void addCUFile(SrcFile file) {
        SrcFile e = new SrcFile(file.getName());
        e.setNotable(file.isNotable());
        for (String lang : file.getLangs()) {
            e.addLang(lang);
        }
        for (String include : file.getIncludes()) {
            e.addImport(include);
        }
        ContainedFiles.add(e);
    }

    public void addInstruction(String command, String workingDir) {
        InstructionSet.add(new Instruction(command, workingDir));
    }

    public void addLang(String lang) {
        Langs.add(lang);
    }

    public void addAllLangs(Collection<String> langs) {
        for (String lang : langs) {
            addLang(lang);
        }
    }

    public HashSet<String> getLangs() {
        return Langs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CompilationUnit [Name=");
        builder.append(Name);
        builder.append(", dummySpec=");
        builder.append(dummySpec);
        builder.append(", PrincipalLang=");
        builder.append(PrincipalLang);
        builder.append(", Langs=");
        builder.append(Langs);
        builder.append(", ContainedFiles=");
        builder.append(ContainedFiles);
        builder.append(", InstructionSet=");
        builder.append(InstructionSet);
        builder.append("]");
        return builder.toString();
    }
}
