package net.persister.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * @author Park, chanwook
 *
 */
@Entity
@SequenceGenerator(name = "ArticleId_identity", sequenceName = "ArticleId_identity")
public class Article {

    @Id
    @GeneratedValue(generator = "ArticleId_identity", strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
