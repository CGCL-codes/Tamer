package au.edu.uq.itee.eresearch.dimer.webapp.app.view.mets;

import static au.edu.uq.itee.eresearch.dimer.webapp.app.view.mets.METSUtils.mods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.jdom2.Content;
import org.jdom2.Element;
import au.edu.uq.itee.eresearch.dimer.webapp.app.view.xml.XMLFragment;

public class MODSFragment implements XMLFragment {

    public static class Identifier {

        private final String type;

        private final String text;

        public Identifier(String type, String text) {
            this.type = type;
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public String getText() {
            return text;
        }
    }

    public static class RelatedItem {

        private final String type;

        private final String publisher;

        private final String url;

        public RelatedItem(String type, String publisher, String url) {
            this.type = type;
            this.publisher = publisher;
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getURL() {
            return url;
        }
    }

    public static class Name {

        private final String type;

        private final String familyName;

        private final String givenName;

        private final String role;

        public Name(String type, String familyName, String givenName, String role) {
            this.type = type;
            this.familyName = familyName;
            this.givenName = givenName;
            this.role = role;
        }

        public String getType() {
            return type;
        }

        public String getFamilyName() {
            return familyName;
        }

        public String getGivenName() {
            return givenName;
        }

        public String getRole() {
            return role;
        }
    }

    private String title;

    private String abstractText;

    private String genre;

    private final List<Identifier> identifiers;

    private final List<RelatedItem> relatedItems;

    private final List<Name> names;

    public MODSFragment() {
        this.title = null;
        this.abstractText = null;
        this.genre = null;
        this.identifiers = new ArrayList<Identifier>();
        this.relatedItems = new ArrayList<RelatedItem>();
        this.names = new ArrayList<Name>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void addIdentifier(Identifier identifier) {
        identifiers.add(identifier);
    }

    public void addRelatedItem(RelatedItem relatedItem) {
        relatedItems.add(relatedItem);
    }

    public void addName(Name name) {
        names.add(name);
    }

    public Collection<Content> getContent() {
        Element element = new Element("mods", mods);
        element.addNamespaceDeclaration(mods);
        if (title != null) {
            element.addContent(new Element("titleInfo", mods).addContent(new Element("title", mods).setText(title)));
        }
        if (abstractText != null) {
            element.addContent(new Element("abstract", mods).setText(abstractText));
        }
        if (genre != null) {
            element.addContent(new Element("genre", mods).setText(genre));
        }
        for (Identifier identifier : identifiers) {
            element.addContent(new Element("identifier", mods).setAttribute("type", identifier.getType()).setText(identifier.getText()));
        }
        for (RelatedItem relatedItem : relatedItems) {
            element.addContent(new Element("relatedItem", mods).setAttribute("type", relatedItem.getType()).addContent(new Element("originInfo", mods).addContent(new Element("publisher", mods).setText(relatedItem.getPublisher()))).addContent(new Element("location", mods).addContent(new Element("url", mods).setText(relatedItem.getURL()))));
        }
        for (Name name : names) {
            element.addContent(new Element("name", mods).setAttribute("type", name.getType()).addContent(new Element("namePart", mods).setText(name.getFamilyName() + ", " + name.getGivenName())).addContent(new Element("role", mods).addContent(new Element("roleTerm", mods).setAttribute("type", "text").setText(name.getRole()))));
        }
        return Arrays.asList((Content) element);
    }
}
