package org.apache.jetspeed.xml.api.jcm;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class Channel implements java.io.Serializable {

    private Topics _topics;

    private java.util.Vector _itemList;

    private java.lang.String _title;

    private java.lang.String _link;

    private java.lang.String _description;

    private Image _image;

    private Textinput _textinput;

    private java.lang.String _rating;

    private java.lang.String _copyright;

    private java.lang.String _pubDate;

    private java.lang.String _lastBuildDate;

    private java.lang.String _docs;

    private java.lang.String _managingEditor;

    private java.lang.String _webMaster;

    private java.lang.String _language;

    public Channel() {
        super();
        _itemList = new Vector();
    }

    /**
     * 
     * @param vItem
    **/
    public void addItem(Item vItem) throws java.lang.IndexOutOfBoundsException {
        _itemList.addElement(vItem);
    }

    /**
    **/
    public java.util.Enumeration enumerateItem() {
        return _itemList.elements();
    }

    /**
    **/
    public java.lang.String getCopyright() {
        return this._copyright;
    }

    /**
    **/
    public java.lang.String getDescription() {
        return this._description;
    }

    /**
    **/
    public java.lang.String getDocs() {
        return this._docs;
    }

    /**
    **/
    public Image getImage() {
        return this._image;
    }

    /**
     * 
     * @param index
    **/
    public Item getItem(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (Item) _itemList.elementAt(index);
    }

    /**
    **/
    public Item[] getItem() {
        int size = _itemList.size();
        Item[] mArray = new Item[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Item) _itemList.elementAt(index);
        }
        return mArray;
    }

    /**
    **/
    public int getItemCount() {
        return _itemList.size();
    }

    /**
    **/
    public java.lang.String getLanguage() {
        return this._language;
    }

    /**
    **/
    public java.lang.String getLastBuildDate() {
        return this._lastBuildDate;
    }

    /**
    **/
    public java.lang.String getLink() {
        return this._link;
    }

    /**
    **/
    public java.lang.String getManagingEditor() {
        return this._managingEditor;
    }

    /**
    **/
    public java.lang.String getPubDate() {
        return this._pubDate;
    }

    /**
    **/
    public java.lang.String getRating() {
        return this._rating;
    }

    /**
    **/
    public Textinput getTextinput() {
        return this._textinput;
    }

    /**
    **/
    public java.lang.String getTitle() {
        return this._title;
    }

    /**
    **/
    public Topics getTopics() {
        return this._topics;
    }

    /**
    **/
    public java.lang.String getWebMaster() {
        return this._webMaster;
    }

    /**
    **/
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
    **/
    public void removeAllItem() {
        _itemList.removeAllElements();
    }

    /**
     * 
     * @param index
    **/
    public Item removeItem(int index) {
        Object obj = _itemList.elementAt(index);
        _itemList.removeElementAt(index);
        return (Item) obj;
    }

    /**
     * 
     * @param copyright
    **/
    public void setCopyright(java.lang.String copyright) {
        this._copyright = copyright;
    }

    /**
     * 
     * @param description
    **/
    public void setDescription(java.lang.String description) {
        this._description = description;
    }

    /**
     * 
     * @param docs
    **/
    public void setDocs(java.lang.String docs) {
        this._docs = docs;
    }

    /**
     * 
     * @param image
    **/
    public void setImage(Image image) {
        this._image = image;
    }

    /**
     * 
     * @param index
     * @param vItem
    **/
    public void setItem(int index, Item vItem) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _itemList.setElementAt(vItem, index);
    }

    /**
     * 
     * @param itemArray
    **/
    public void setItem(Item[] itemArray) {
        _itemList.removeAllElements();
        for (int i = 0; i < itemArray.length; i++) {
            _itemList.addElement(itemArray[i]);
        }
    }

    /**
     * 
     * @param language
    **/
    public void setLanguage(java.lang.String language) {
        this._language = language;
    }

    /**
     * 
     * @param lastBuildDate
    **/
    public void setLastBuildDate(java.lang.String lastBuildDate) {
        this._lastBuildDate = lastBuildDate;
    }

    /**
     * 
     * @param link
    **/
    public void setLink(java.lang.String link) {
        this._link = link;
    }

    /**
     * 
     * @param managingEditor
    **/
    public void setManagingEditor(java.lang.String managingEditor) {
        this._managingEditor = managingEditor;
    }

    /**
     * 
     * @param pubDate
    **/
    public void setPubDate(java.lang.String pubDate) {
        this._pubDate = pubDate;
    }

    /**
     * 
     * @param rating
    **/
    public void setRating(java.lang.String rating) {
        this._rating = rating;
    }

    /**
     * 
     * @param textinput
    **/
    public void setTextinput(Textinput textinput) {
        this._textinput = textinput;
    }

    /**
     * 
     * @param title
    **/
    public void setTitle(java.lang.String title) {
        this._title = title;
    }

    /**
     * 
     * @param topics
    **/
    public void setTopics(Topics topics) {
        this._topics = topics;
    }

    /**
     * 
     * @param webMaster
    **/
    public void setWebMaster(java.lang.String webMaster) {
        this._webMaster = webMaster;
    }

    /**
     * 
     * @param reader
    **/
    public static org.apache.jetspeed.xml.api.jcm.Channel unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.apache.jetspeed.xml.api.jcm.Channel) Unmarshaller.unmarshal(org.apache.jetspeed.xml.api.jcm.Channel.class, reader);
    }

    /**
    **/
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
