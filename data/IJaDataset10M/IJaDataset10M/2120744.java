package org.localstorm.mcc.web.gtd.actions;

import org.localstorm.mcc.web.gtd.GtdBaseActionBean;
import org.localstorm.mcc.web.gtd.backend.RefObjectCollector;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.RAMDirectory;
import org.localstorm.mcc.ejb.gtd.entity.FileAttachment;
import org.localstorm.mcc.ejb.gtd.FileManager;
import org.localstorm.mcc.ejb.gtd.entity.Note;
import org.localstorm.mcc.ejb.gtd.NoteManager;
import org.localstorm.mcc.ejb.gtd.RefObjectManager;
import org.localstorm.mcc.ejb.gtd.entity.ReferencedObject;
import org.localstorm.mcc.ejb.users.User;
import org.localstorm.mcc.web.gtd.Views;
import org.localstorm.mcc.web.gtd.actions.wrap.RoSearchResult;
import org.localstorm.tools.aop.runtime.Logged;

/**
 * @author Alexey Kuznetsov
 */
@UrlBinding("/actions/gtd/nil/SubmitRefObjAttachSearch")
public class RefObjAttachmentSearchSubmitActionBean extends GtdBaseActionBean {

    private static final String FILE_TYPE = "FILE";

    private static final String TYPE_FIELD = "type";

    private static final String ID_FIELD = "id";

    private static final String SEARCHABLE = "text";

    private static final char SPACE = ' ';

    private boolean found;

    @Validate(required = true)
    private String text;

    private Collection<? extends Note> objectTextualNotes;

    private Collection<? extends Note> objectUrlNotes;

    private Collection<FileAttachment> objectFiles;

    public Collection<FileAttachment> getObjectFiles() {
        return objectFiles;
    }

    public Collection<? extends Note> getObjectTextualNotes() {
        return objectTextualNotes;
    }

    public Collection<? extends Note> getObjectUrlNotes() {
        return objectUrlNotes;
    }

    public void setObjectFiles(Collection<FileAttachment> objectFiles) {
        this.objectFiles = objectFiles;
    }

    public void setObjectTextualNotes(Collection<? extends Note> objectTextualNotes) {
        this.objectTextualNotes = objectTextualNotes;
    }

    public void setObjectUrlNotes(Collection<? extends Note> objectUrlNotes) {
        this.objectUrlNotes = objectUrlNotes;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @DefaultHandler
    @Logged
    public Resolution filling() throws Exception {
        this.objectFiles = new LinkedList<FileAttachment>();
        this.objectTextualNotes = new LinkedList<Note>();
        this.objectUrlNotes = new LinkedList<Note>();
        RefObjectManager rm = super.getRefObjectManager();
        User user = this.getUser();
        Collection<ReferencedObject> ros = rm.findAllByOwner(user);
        Collection<Note> notes = new LinkedList<Note>();
        Collection<FileAttachment> files = new LinkedList<FileAttachment>();
        Map<Integer, ReferencedObject> note2ro = new HashMap<Integer, ReferencedObject>();
        Map<Integer, ReferencedObject> file2ro = new HashMap<Integer, ReferencedObject>();
        RAMDirectory ramd = this.fetchRamDirectory(ros, notes, files, note2ro, file2ro);
        this.setFound(false);
        RoSearchResult result = this.search(ramd, ros, notes, files, note2ro, file2ro, text);
        this.setFound(!result.isEmpty());
        if (this.isFound()) {
            this.setObjectFiles(result.getFilesFound());
            this.setObjectTextualNotes(result.getTextualNotesFound());
            this.setObjectUrlNotes(result.getUrlNotesFound());
        }
        return new ForwardResolution(Views.SEARCH_RO_ATTACH);
    }

    private RAMDirectory fetchRamDirectory(Collection<ReferencedObject> ros, Collection<Note> outNotes, Collection<FileAttachment> outFiles, Map<Integer, ReferencedObject> note2ro, Map<Integer, ReferencedObject> file2ro) throws IOException {
        FileManager fm = super.getFileManager();
        NoteManager nm = super.getNoteManager();
        RAMDirectory ramd = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(ramd, new StandardAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
        for (ReferencedObject ro : ros) {
            Collection<Note> notes = this.getNotesByRo(ro, nm);
            Collection<FileAttachment> files = this.getFileAttachmentsByRo(ro, fm);
            for (Note note : notes) {
                outNotes.add(note);
                note2ro.put(note.getId(), ro);
                String searchable = this.getSearchable(note, ro);
                Document doc = new Document();
                {
                    doc.add(new Field(SEARCHABLE, searchable, Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(ID_FIELD, note.getId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(TYPE_FIELD, note.getType(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                }
                indexWriter.addDocument(doc);
            }
            for (FileAttachment file : files) {
                outFiles.add(file);
                file2ro.put(file.getId(), ro);
                Document doc = new Document();
                {
                    String searchable = this.getSearchable(file, ro);
                    doc.add(new Field(SEARCHABLE, searchable, Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(ID_FIELD, file.getId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(TYPE_FIELD, FILE_TYPE, Field.Store.YES, Field.Index.NOT_ANALYZED));
                }
                indexWriter.addDocument(doc);
            }
        }
        indexWriter.close();
        return ramd;
    }

    private Collection<FileAttachment> getFileAttachmentsByRo(ReferencedObject ro, FileManager fm) {
        return fm.findAllByObject(ro);
    }

    private Collection<Note> getNotesByRo(ReferencedObject ro, NoteManager nm) {
        return nm.findAllByObject(ro);
    }

    private String getSearchable(FileAttachment file, ReferencedObject ro) {
        StringBuilder sb = new StringBuilder();
        sb.append(ro.getContext().getName());
        sb.append(SPACE);
        sb.append(ro.getName());
        sb.append(SPACE);
        sb.append(file.getName());
        sb.append(SPACE);
        sb.append(file.getDescription());
        if (file.getMimeType() != null) {
            sb.append(SPACE);
            sb.append(file.getMimeType());
        }
        String searchable = sb.toString();
        return searchable;
    }

    private String getSearchable(Note note, ReferencedObject ro) {
        StringBuilder sb = new StringBuilder();
        sb.append(ro.getContext().getName());
        sb.append(SPACE);
        sb.append(ro.getName());
        sb.append(SPACE);
        sb.append(note.getNote());
        sb.append(SPACE);
        sb.append(note.getDescription());
        String searchable = sb.toString();
        return searchable;
    }

    private RoSearchResult search(RAMDirectory ramd, Collection<ReferencedObject> ros, Collection<Note> notes, Collection<FileAttachment> files, Map<Integer, ReferencedObject> note2ro, Map<Integer, ReferencedObject> file2ro, String text) throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        IndexSearcher is = new IndexSearcher(ramd);
        QueryParser parser = new QueryParser(SEARCHABLE, analyzer);
        Query query = parser.parse(text);
        RefObjectCollector roc = new RefObjectCollector(is, ID_FIELD, TYPE_FIELD, ros, notes, files, note2ro, file2ro);
        is.search(query, roc);
        return roc.getSearchResult();
    }
}
