package sample.capelin.transaction;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.capelin.core.models.CapelinMARCRecord;
import org.hibernate.annotations.Entity;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * 
 * <a href="http://code.google.com/p/capline-opac/">Capelin-opac</a>
 * License: GNU AGPL v3 | http://www.gnu.org/licenses/agpl.html 
 * 
 * Sample of transaction Record.
 * The class may omit when Hibernate Search support xml configuration.
 * 
 * @author Jing Xiao <jing.xiao.ca at gmail dot com>
 */
@Entity
@Indexed
public class SampleTxRecord extends CapelinMARCRecord {

    @DocumentId
    protected int id;

    @Field(index = Index.TOKENIZED, store = Store.YES)
    protected String author;

    @Field(store = Store.YES)
    @Analyzer(impl = StandardAnalyzer.class)
    protected String subject;

    @Field(index = Index.TOKENIZED, store = Store.YES)
    protected String title;

    @Field(index = Index.TOKENIZED, store = Store.YES)
    @Boost(1.5f)
    protected String documentType;

    @Field(index = Index.UN_TOKENIZED)
    @Boost(1.5f)
    protected String isbn;

    @Field(index = Index.UN_TOKENIZED)
    @Boost(1.5f)
    protected String issn;

    public SampleTxRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
