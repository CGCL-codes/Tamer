package com.lowagie.text;

import com.lowagie.text.factories.GreekAlphabetFactory;

/**
 * 
 * A special-version of <CODE>LIST</CODE> which use greek-letters.
 * 
 * @see com.lowagie.text.List
 */
public class GreekList extends List {

    /**
	 * Initialization
	 */
    public GreekList() {
        super(true);
        setGreekFont();
    }

    /**
	 * Initialization
	 * 
	 * @param symbolIndent	indent
	 */
    public GreekList(int symbolIndent) {
        super(true, symbolIndent);
        setGreekFont();
    }

    /**
	 * Initialization 
	 * @param	greeklower		greek-char in lowercase   
	 * @param 	symbolIndent	indent
	 */
    public GreekList(boolean greeklower, int symbolIndent) {
        super(true, symbolIndent);
        lowercase = greeklower;
        setGreekFont();
    }

    /**
	 * change the font to SYMBOL
	 */
    protected void setGreekFont() {
        float fontsize = symbol.getFont().getSize();
        symbol.setFont(FontFactory.getFont(FontFactory.SYMBOL, fontsize, Font.NORMAL));
    }

    /**
	 * Adds an <CODE>Object</CODE> to the <CODE>List</CODE>.
	 *
	 * @param	o	the object to add.
	 * @return true if adding the object succeeded
	 */
    public boolean add(Object o) {
        if (o instanceof ListItem) {
            ListItem item = (ListItem) o;
            Chunk chunk = new Chunk(preSymbol, symbol.getFont());
            chunk.append(GreekAlphabetFactory.getString(first + list.size(), lowercase));
            chunk.append(postSymbol);
            item.setListSymbol(chunk);
            item.setIndentationLeft(symbolIndent, autoindent);
            item.setIndentationRight(0);
            list.add(item);
        } else if (o instanceof List) {
            List nested = (List) o;
            nested.setIndentationLeft(nested.getIndentationLeft() + symbolIndent);
            first--;
            return list.add(nested);
        } else if (o instanceof String) {
            return this.add(new ListItem((String) o));
        }
        return false;
    }
}
