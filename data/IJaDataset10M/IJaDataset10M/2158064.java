package com.lizardtech.djvubean.text;

import com.lizardtech.djvu.Document;
import com.lizardtech.djvu.text.*;
import com.lizardtech.djvu.DjVuOptions;
import com.lizardtech.djvubean.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * This is an optional component neccissary to add search features to the the
 * DjVuBean.  The run() method is used to invoke a search with the
 * parameters specified in the DjVuBean.
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class TextSearch implements java.io.Serializable, Runnable, PropertyChangeListener {

    /** DjVuBean to search. */
    protected final DjVuBean djvuBean;

    private Object searchObject = null;

    private int emptyCount = 0;

    /**
   * Creates a new TextSearch object.
   *
   * @param bean DjVuBean to search.
   */
    public TextSearch(final DjVuBean bean) {
        djvuBean = bean;
        bean.properties.put("addOn.text", TextSearch.class.getName());
        bean.addPropertyChangeListener(this);
    }

    /**
   * Query the DjVuText data for the specified currentPage.
   * 
   * @param page number to read the text from.
   * 
   * @return DjVuText data for the specified currentPage.
   */
    public DjVuText getDjVuText(final int page) {
        try {
            if (page == djvuBean.getPage()) {
                return (DjVuText) djvuBean.getTextCodec(0L);
            } else {
                final Document document = djvuBean.getDocument();
                return DjVuText.createDjVuText(document).init(document.get_data((page - 1) % djvuBean.getDocumentSize()));
            }
        } catch (final IOException exp) {
            exp.printStackTrace(DjVuOptions.err);
            System.gc();
        }
        return null;
    }

    /**
   * Called when a property has changed.
   *
   * @param event describing the property change.
   */
    public void propertyChange(final PropertyChangeEvent event) {
        try {
            final String name = event.getPropertyName();
            if ("page".equalsIgnoreCase(name) || "searchMask".equalsIgnoreCase(name) || "searchText".equalsIgnoreCase(name)) {
                searchObject = null;
            }
        } catch (final Throwable exp) {
            exp.printStackTrace(DjVuOptions.err);
            System.gc();
        }
    }

    /**
   * Search for text forward or backwards from the last caret position in the
   * whole document or currentPage.
   */
    public void run() {
        final String searchText = djvuBean.getSearchText();
        if ((searchText == null) || (searchText.length() == 0) || (emptyCount >= djvuBean.getDocumentSize())) {
            djvuBean.setCaretPosition(-1);
            djvuBean.setCaretIndex(-1);
            return;
        }
        int mask = djvuBean.getSearchMask();
        final int caretPosition = djvuBean.getCaretPosition();
        final int caretIndex = djvuBean.getCaretIndex();
        for (; ; ) {
            final Object searchObject = new Object();
            this.searchObject = searchObject;
            emptyCount = 0;
            final int indexMax = djvuBean.getVisiblePageCount();
            int retval = -1;
            if ((mask & DjVuBean.SEARCH_BACKWARD_MASK) == 0) {
                int index = djvuBean.getCaretIndex();
                if (index < 0) {
                    index = 0;
                }
                while (index < indexMax) {
                    final DjVuText djvuText = (DjVuText) djvuBean.getImageWait().getTextCodec(index, 0L);
                    retval = searchPage(index, djvuText, searchText, mask);
                    if (retval != -1) {
                        break;
                    }
                    index++;
                }
            } else {
                int index = djvuBean.getCaretIndex();
                if (index < 0) {
                    index = djvuBean.getVisiblePageCount() - 1;
                }
                while (index > 0) {
                    final DjVuText djvuText = (DjVuText) djvuBean.getTextCodec(index, 0L);
                    retval = searchPage(index, djvuText, searchText, mask);
                    if (retval != -1) {
                        break;
                    }
                    index--;
                }
            }
            if (retval == -1) {
                if (((mask & DjVuBean.WHOLE_DOCUMENT_MASK) != 0) && (searchObject == this.searchObject)) {
                    final int size = djvuBean.getDocumentSize();
                    final Vector selectionList = new Vector();
                    final int currentPage = djvuBean.getPage();
                    int pageMin = currentPage - ((currentPage - djvuBean.getPageOffset() - 1) % djvuBean.getVisiblePageCount());
                    int pageMax = pageMin + djvuBean.getVisiblePageCount() - 1;
                    if (pageMin < 1) {
                        pageMin = 1;
                    }
                    if (pageMax > size + 1) {
                        pageMax = size + 1;
                    }
                    if ((mask & DjVuBean.SEARCH_BACKWARD_MASK) == 0) {
                        for (int page = ((pageMax + 1) % size) + 1; (page != pageMin) && (currentPage == djvuBean.getPage()); page = (page % size) + 1) {
                            DjVuOptions.out.println("Search page=" + page + " for <" + searchText + ">");
                            final DjVuText djvuText = getDjVuText(page);
                            if (searchObject != this.searchObject) {
                                break;
                            }
                            if (search_string(djvuText, selectionList, searchText, -1, mask) != -1) {
                                djvuBean.setPage(page);
                                break;
                            }
                        }
                    } else {
                        for (int page = ((pageMin + size - 1) % size) + 1; (page != pageMax) && (currentPage == djvuBean.getPage()); page = ((page + size - 2) % size) + 1) {
                            DjVuOptions.out.println("Search page=" + page + " for <" + searchText + ">");
                            final DjVuText djvuText = getDjVuText(page);
                            if (searchObject != this.searchObject) {
                                break;
                            }
                            if (search_string(djvuText, selectionList, searchText, -1, mask) != -1) {
                                djvuBean.setPage(page);
                                break;
                            }
                        }
                    }
                    if ((currentPage != djvuBean.getPage()) && (this.searchObject == null)) {
                        mask = mask & (~DjVuBean.WHOLE_DOCUMENT_MASK);
                        djvuBean.setCaretPosition(-1);
                        djvuBean.setCaretIndex(-1);
                        continue;
                    }
                } else {
                    djvuBean.setCaretPosition(-1);
                    djvuBean.setCaretIndex(-1);
                }
            }
            break;
        }
        if ((djvuBean.getCaretPosition() == caretPosition) && (djvuBean.getCaretIndex() == caretIndex)) {
            djvuBean.setCaretPosition(-1);
            djvuBean.setCaretIndex(-1);
        }
    }

    /**
   * Search for text forward or backwards from the last Caret Position in the
   * current currentPage.
   * 
   * @param index the currentPage index being searched
   * @param djvuText to search.
   * @param searchText text to search for.
   * @param mask of search options.
   * 
   * @return the position where the text was found, or -1 if the text was not
   *         found.
   */
    private int searchPage(final int index, final DjVuText djvuText, final String searchText, final int mask) {
        if (djvuText == null) {
            emptyCount++;
            return -1;
        }
        final String textTrim = searchText.trim();
        final Vector selectionList = new Vector();
        final int caretPosition = search_string(djvuText, selectionList, textTrim, (index == djvuBean.getCaretIndex()) ? djvuBean.getCaretPosition() : (-1), mask);
        if (index >= 0) {
            djvuBean.setCaretPosition(caretPosition);
            djvuBean.setCaretIndex(index);
            djvuBean.setSelectionList(index, selectionList);
            if (caretPosition >= 0) {
                final Rectangle rect = djvuBean.getImageWait().getHighlightBounds();
                final Rectangle scrollPaneBounds = new Rectangle(djvuBean.getScrollPosition(), djvuBean.getViewportSize());
                final boolean willContain = ((scrollPaneBounds.x <= rect.x) && (scrollPaneBounds.x + scrollPaneBounds.width >= rect.x + rect.width) && (scrollPaneBounds.y + scrollPaneBounds.height >= rect.y + rect.height));
                if (willContain) {
                    if (scrollPaneBounds.y != rect.y) {
                        djvuBean.setScrollPosition(scrollPaneBounds.x, rect.y);
                    }
                } else if (rect.width >= scrollPaneBounds.width) {
                    djvuBean.setScrollPosition(rect.x, rect.y);
                } else {
                    djvuBean.setScrollPosition((((2 * rect.x) + rect.width) - scrollPaneBounds.width) / 2, rect.y);
                }
                final int startChar = djvuText.getLength(0, caretPosition);
                final int endByte = djvuText.startsWith(searchText, caretPosition, ((mask & DjVuBean.MATCH_CASE_MASK) != 0));
                final int endChar = startChar + djvuText.getLength(caretPosition, endByte);
                if (startChar >= 0) {
                    try {
                        final TextArea textArea = djvuBean.getTextArea();
                        if (endChar > startChar) {
                            try {
                                textArea.setCaretPosition(endChar);
                            } catch (final Throwable ignored) {
                            }
                        }
                        textArea.setCaretPosition(startChar);
                        textArea.select(startChar, endChar);
                    } catch (final Throwable ignored) {
                        ignored.printStackTrace(DjVuOptions.err);
                    }
                }
            }
        }
        return caretPosition;
    }

    private int search_string(final DjVuText djvuText, final Vector selectionList, final String textTrim, final int from, final int mask) {
        int retval = -1;
        if (djvuText != null) {
            try {
                retval = djvuText.search_string(selectionList, textTrim, from, ((mask & DjVuBean.SEARCH_BACKWARD_MASK) == 0), ((mask & DjVuBean.MATCH_CASE_MASK) != 0), ((mask & DjVuBean.WHOLE_WORD_MASK) != 0));
            } catch (Throwable exp) {
                exp.printStackTrace(DjVuOptions.err);
                System.gc();
            }
        } else {
            emptyCount++;
        }
        return retval;
    }
}
