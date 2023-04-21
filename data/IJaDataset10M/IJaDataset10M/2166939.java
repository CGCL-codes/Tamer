package de.sendorian.app.forumArchive.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import de.sendorian.app.forumArchive.ApplicationContextHolder;
import de.sendorian.app.forumArchive.Config;

@Entity
public class Forum {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @Column(length = 10000)
    private String url;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Topic> topics = new HashSet<Topic>();

    private int pages;

    private static final Pattern uriPattern = Pattern.compile(".*://.*");

    public Forum() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name.replace("/", ",").replace("\\", ",");
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (!uriPattern.matcher(url).matches()) {
            Config config = (Config) ApplicationContextHolder.getBean("config");
            this.url = config.getBaseUrl() + url;
        } else {
            this.url = url;
        }
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Forum \"" + getName() + "\" (" + getUrl() + ")";
    }
}
