package net.sf.lightbound.opencms.content;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import net.sf.lightbound.AssociationContainer;
import net.sf.lightbound.PageOutput;
import net.sf.lightbound.Renderable;
import net.sf.lightbound.exceptions.TranslationException;
import net.sf.lightbound.util.ChainedIterator;
import net.sf.lightbound.util.IterableImpl;
import org.opencms.jsp.CmsJspXmlContentBean;
import org.opencms.jsp.I_CmsXmlContentContainer;
import org.opencms.main.CmsRuntimeException;
import org.opencms.xml.CmsXmlException;
import org.opencms.xml.I_CmsXmlDocument;

public class ContentContainerAssociation implements AssociationContainer {

    private static final Iterable<ContentContainerAssociation> EMPTY_ITERABLE = new Iterable<ContentContainerAssociation>() {

        public Iterator<ContentContainerAssociation> iterator() {
            return new Iterator<ContentContainerAssociation>() {

                public void remove() {
                }

                public ContentContainerAssociation next() {
                    return null;
                }

                public boolean hasNext() {
                    return false;
                }
            };
        }
    };

    private static final String ROOT_CONTAINER_NAME = "singleFile";

    private final CmsJspXmlContentBean contentBean;

    private final I_CmsXmlContentContainer content;

    public ContentContainerAssociation(PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws JspException {
        this(new CmsJspXmlContentBean(pageContext, request, response));
    }

    public ContentContainerAssociation(CmsJspXmlContentBean contentBean) throws JspException {
        this(contentBean, loadContent(contentBean));
    }

    @SuppressWarnings("unchecked")
    public ContentContainerAssociation(CmsJspXmlContentBean contentBean, I_CmsXmlContentContainer content) {
        this.contentBean = contentBean;
        this.content = content;
    }

    public boolean exists() {
        return content != null;
    }

    public Iterable<ContentContainerAssociation> getChildren(String key) {
        if (content == null) {
            return EMPTY_ITERABLE;
        }
        final I_CmsXmlContentContainer loopContainer = getContentLoop(key);
        if (loopContainer == null) {
            return EMPTY_ITERABLE;
        }
        final ContentContainerAssociation childAssociations = getChildRenderer(loopContainer);
        final I_CmsXmlContentContainer loopContainer2 = getContentLoop(key);
        boolean hasAnything;
        try {
            hasAnything = loopContainer.hasMoreContent();
            loopContainer2.hasMoreContent();
        } catch (JspException e) {
            throw new RuntimeException(e);
        }
        final boolean hasAnythingFinal = hasAnything;
        return new Iterable<ContentContainerAssociation>() {

            public Iterator<ContentContainerAssociation> iterator() {
                return new Iterator<ContentContainerAssociation>() {

                    private boolean first = true;

                    private boolean hasNext = hasAnythingFinal;

                    public void remove() {
                        throw new UnsupportedOperationException("remove unsupported");
                    }

                    public ContentContainerAssociation next() {
                        checkHasNext();
                        return childAssociations;
                    }

                    public boolean hasNext() {
                        return hasNext;
                    }

                    private void checkHasNext() {
                        try {
                            if (first) {
                                first = false;
                            } else {
                                loopContainer.hasMoreContent();
                            }
                            hasNext = loopContainer2.hasMoreContent();
                        } catch (JspException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        };
    }

    public Iterable<ContentContainerAssociation> search(String key) {
        Iterator<ContentContainerAssociation> results = doSearch(key);
        if (results == null) {
            return null;
        }
        return new IterableImpl<ContentContainerAssociation>(results);
    }

    public boolean hasValue() {
        return getStringValue() != null;
    }

    public String getStringValue() {
        if (content == null) {
            return null;
        }
        return getStringValue(content);
    }

    public int getIntValue() {
        String stringValue = getStringValue();
        if (stringValue == null) {
            return 0;
        }
        return Integer.parseInt(stringValue);
    }

    public long getLongValue() {
        String stringValue = getStringValue();
        if (stringValue == null) {
            return 0;
        }
        return Long.parseLong(stringValue);
    }

    public float getFloatValue() {
        String stringValue = getStringValue();
        if (stringValue == null) {
            return 0.0f;
        }
        return Float.parseFloat(stringValue);
    }

    public double getDoubleValue() {
        String stringValue = getStringValue();
        if (stringValue == null) {
            return 0.0;
        }
        return Double.parseDouble(stringValue);
    }

    public Iterable<ContentContainerAssociation> getAssociatedObject(final String key) {
        if (content == null) {
            return null;
        }
        return getChildren(key);
    }

    private ContentContainerAssociation getChildRenderer(I_CmsXmlContentContainer container) {
        return new ContentContainerAssociation(contentBean, container);
    }

    private String getStringValue(I_CmsXmlContentContainer container) {
        I_CmsXmlDocument xmlContent = container.getXmlDocument();
        if (xmlContent == null) {
            return null;
        }
        String element = container.getXmlDocumentElement();
        if (element == null) {
            return null;
        }
        if (!xmlContent.hasValue(element, container.getXmlDocumentLocale())) {
            return null;
        }
        try {
            xmlContent.getStringValue(contentBean.getCmsObject(), element, container.getXmlDocumentLocale());
        } catch (CmsXmlException e) {
            return null;
        } catch (CmsRuntimeException e) {
            return null;
        }
        return contentBean.contentshow(container);
    }

    private I_CmsXmlContentContainer getContentLoop(String childKey) {
        String valueName = childKey;
        String currentName = content.getXmlDocumentElement();
        if (currentName != null) {
            valueName = currentName + "/" + valueName;
        }
        if (!content.getXmlDocument().hasValue(valueName, content.getXmlDocumentLocale())) {
            return null;
        }
        return contentBean.contentloop(content, childKey);
    }

    public I_CmsXmlContentContainer getContent() {
        return content;
    }

    private static I_CmsXmlContentContainer loadContent(CmsJspXmlContentBean contentBean) throws JspException {
        I_CmsXmlContentContainer content = contentBean.contentload(ROOT_CONTAINER_NAME, contentBean.getRequestContext().getUri(), false);
        boolean exists = content.hasMoreContent();
        if (!exists) {
            return null;
        }
        return content;
    }

    @SuppressWarnings("unchecked")
    private Iterator<ContentContainerAssociation> doSearch(String key) {
        if (key == null) {
            return null;
        }
        I_CmsXmlDocument doc = content.getXmlDocument();
        if (doc == null) {
            return null;
        }
        Iterator<ContentContainerAssociation> results = null;
        List<String> names = doc.getNames(content.getXmlDocumentLocale());
        for (String name : names) {
            Iterable<ContentContainerAssociation> children = getChildren(name);
            if (key.equals(name)) {
                results = ChainedIterator.getIterator(results, children.iterator());
            }
            for (ContentContainerAssociation child : children) {
                Iterator<ContentContainerAssociation> childResults = child.doSearch(key);
                if (childResults != null) {
                    results = ChainedIterator.getIterator(results, childResults);
                }
            }
        }
        return results;
    }
}
