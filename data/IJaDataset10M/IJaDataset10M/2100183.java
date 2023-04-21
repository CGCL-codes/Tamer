package commonapp.docobj;

/**
   Implements a document container for tables, figures, lists, examples,
   verbatim text, etc.
*/
public class ReportContainer extends BaseReport {

    protected ReportRoot myRoot;

    protected String myTagDef = null;

    protected boolean myTextCompleted = false;

    protected String mySectionNumber;

    /**
     Constructs a new ReportContainer.

     @param theParent the base report.

     @param theType the report type.

     @param theAddFlag set true if this container will be added to the parent.
  */
    protected ReportContainer(BaseReport theParent, String theType, boolean theAddFlag) {
        super(theParent, theType, theAddFlag);
        myRoot = getRoot();
    }

    /**
     Starts a new paragraph in this container.

     @return the tag definition associated with the paragraph or null if
     the paragraph was not created.
  */
    public String paragraph() {
        myTagDef = null;
        if (!myTextCompleted) {
            myTagDef = "_p" + String.valueOf(++myRoot.myParaCount);
            myRoot.addSecNum(myTagDef, mySectionNumber);
            setTextObj(XMLElement.PARA.getDocObj(new String[] { XMLElement.A_TAGDEF, myTagDef }));
        }
        return myTagDef;
    }

    /**
     Returns the current tag definition associated with element.  The tag
     name is updated each time a new text element is added, a paragraph, etc.

     @return the current tag definition associated with this element.
  */
    public String getTagDef() {
        return myTagDef;
    }

    /**
     Adds an index term to this container.

     @param theTerm the term to be added.
  */
    public void addIndexTerm(String theTerm) {
        if (myTagDef != null) {
            myRoot.addIndexTerm(myTagDef, theTerm);
            myRoot.addSecNum(myTagDef, mySectionNumber);
        }
    }

    /**
     Adds an ordered (numbered) list to this container.

     @return an ordered (numbered) list.
  */
    public ReportList orderedList() {
        ReportList list = null;
        if (!myTextCompleted) {
            setTextObj(null);
            list = new ReportList(this, "ordered");
        }
        return list;
    }

    /**
     Adds an unordered (bullet) list to this container.

     @return an unordered (bullet) list.
  */
    public ReportList bulletList() {
        ReportList list = null;
        if (!myTextCompleted) {
            setTextObj(null);
            list = new ReportList(this, "bullet");
        }
        return list;
    }

    /**
     Adds a table to this container.

     @param theTitle the table title.

     @param theTip the table summary or tool tip.

     @return the added table.
  */
    public ReportTable table(String theTitle, String theTip) {
        return table(theTitle, theTip, null, null, null, null, null);
    }

    public ReportTable table(String theTitle, String theTip, String theSize, String theColor, String theFace, String theAlign, String theBorder) {
        ReportTable table = null;
        if (!myTextCompleted) {
            setTextObj(null);
            table = new ReportTable(this, theTitle, theTip);
            if (theSize != null) {
                table.putAttr(XMLElement.A_SIZE, theSize);
            }
            if (theColor != null) {
                table.putAttr(XMLElement.A_COLOR, theColor);
            }
            if (theFace != null) {
                table.putAttr(XMLElement.A_FACE, theFace);
            }
            if (theAlign != null) {
                table.putAttr(XMLElement.A_ALIGN, theAlign);
            }
            if (theBorder != null) {
                table.putAttr(XMLElement.A_BORDER, theBorder);
            }
            paragraph();
        }
        return table;
    }

    /**
     Adds a figure to this container.

     @param theTitle the figure title.

     @param theTip the figure summary or tool tip.

     @param theImageFile the name of the file that contains the figure image.

     @return the tag definition associated with the figure or null if the
     figure was not created.
  */
    public String figure(String theTitle, String theTip, String theImageFile) {
        return figure(theTitle, theTip, theImageFile, null, null, null, null);
    }

    public String figure(String theTitle, String theTip, String theImageFile, String theSize, String theColor, String theFace, String theAlign) {
        return figure(theTitle, theTip, theImageFile, theSize, theColor, theFace, theAlign, null);
    }

    public String figure(String theTitle, String theTip, String theImageFile, String theSize, String theColor, String theFace, String theAlign, String theTagDef) {
        String tagDef = null;
        if (!myTextCompleted) {
            setTextObj(null);
            DocObj fig = XMLElement.FIGURE.getDocObj();
            myDocObj.addObj(fig);
            putAttr(fig, XMLElement.A_TITLE, theTitle);
            fig.putAttr(XMLElement.A_COMMENT, theTip);
            fig.putAttr(XMLElement.A_IMAGEFILE, theImageFile);
            myRoot.myFigureCount += 1;
            if ((theTagDef == null) || theTagDef.trim().equals("")) {
                tagDef = "_fig" + String.valueOf(myRoot.myFigureCount);
            } else {
                tagDef = theTagDef;
            }
            fig.putAttr(XMLElement.A_TAGDEF, tagDef);
            if (theSize != null) {
                putAttr(fig, XMLElement.A_SIZE, theSize);
            }
            if (theColor != null) {
                putAttr(fig, XMLElement.A_COLOR, theColor);
            }
            if (theFace != null) {
                putAttr(fig, XMLElement.A_FACE, theFace);
            }
            if (theAlign != null) {
                fig.putAttr(XMLElement.A_ALIGN, theAlign);
            }
            paragraph();
        }
        return tagDef;
    }

    /**
     Returns the number that will be assigned to the next figure added to
     the report.

     @return the number that will be assigned to the next figure added to
     the report.
  */
    public int getNextFigureNumber() {
        return myRoot.myFigureCount + 1;
    }

    /**
     Returns the number that will be assigned to the next table added to
     the report.

     @return the number that will be assigned to the next table added to
     the report.
  */
    public int getNextTableNumber() {
        return myRoot.myTableCount + 1;
    }
}
