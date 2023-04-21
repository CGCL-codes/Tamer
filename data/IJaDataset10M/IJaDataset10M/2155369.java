package br.ufpe.cin.ontocompo.module.model;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.semanticweb.owl.model.OWLEntity;
import br.ufpe.cin.ontocompo.module.owlrender.OWLObjectModuleRenderer;

/**
 * Class Module.java 
 * Represents the module entity 
 *
 * @author Camila Bezerra (kemylle@gmail.com)
 * @date Jul 18, 2008
 */
public class Module implements Serializable {

    private Uses uses;

    private ImportedEntities importedEntities;

    private ExportedEntities exportedEntities;

    private Content content;

    private List<Alignment> alignments;

    private String name;

    private URI uri;

    private URI physicalURI;

    private DescriptionModule description;

    public Module(URI uri) {
        this.uri = uri;
        importedEntities = new ImportedEntities();
        exportedEntities = new ExportedEntities();
        alignments = new ArrayList();
    }

    public Module() {
        importedEntities = new ImportedEntities();
        exportedEntities = new ExportedEntities();
        alignments = new ArrayList();
    }

    public Uses getUses() {
        return uses;
    }

    public void setUses(Uses uses) {
        this.uses = uses;
    }

    public ImportedEntities getImportedEntities() {
        return importedEntities;
    }

    public void setImportedEntities(ImportedEntities importedEntities) {
        this.importedEntities = importedEntities;
    }

    public ExportedEntities getExportedEntities() {
        return exportedEntities;
    }

    public void setExportedEntities(ExportedEntities exportedEntities) {
        this.exportedEntities = exportedEntities;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void accept(OWLObjectModuleRenderer visitor) {
        visitor.visit(this);
    }

    public URI getPhysicalURI() {
        return physicalURI;
    }

    public void setPhysicalURI(URI physicalURI) {
        this.physicalURI = physicalURI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DescriptionModule getDescription() {
        return description;
    }

    public void setDescription(DescriptionModule description) {
        this.description = description;
    }

    public List<Alignment> getAlignments() {
        return alignments;
    }

    public void setAlignments(List<Alignment> alignments) {
        this.alignments = alignments;
    }
}
