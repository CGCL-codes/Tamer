package net.ontopia.topicmaps.webed.impl.actions.occurrence;

import java.net.MalformedURLException;
import java.util.Iterator;
import net.ontopia.infoset.content.ContentStoreException;
import net.ontopia.infoset.content.ContentStoreIF;
import net.ontopia.infoset.content.ContentStoreUtils;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapBuilderIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.utils.QueryUtils;
import net.ontopia.topicmaps.webed.core.ActionIF;
import net.ontopia.topicmaps.webed.core.ActionParametersIF;
import net.ontopia.topicmaps.webed.core.ActionResponseIF;
import net.ontopia.topicmaps.webed.core.ActionRuntimeException;
import net.ontopia.topicmaps.webed.core.FileValueIF;

/**
 * INTERNAL: Action for uploading a file as an external occurrence.
 *
 * @since 2.0.3
 */
public class UploadFile implements ActionIF {

    public void perform(ActionParametersIF params, ActionResponseIF response) {
        try {
            OccurrenceIF occ = (OccurrenceIF) params.get(0);
            TopicMapIF tm = occ.getTopicMap();
            ContentStoreIF store = ContentStoreUtils.getContentStore(tm, null);
            FileValueIF file = params.getFileValue();
            int key = store.add(file.getContents(), (int) file.getLength());
            if (occ.getLocator() != null) {
                String uri = occ.getLocator().getAddress();
                if (uri.startsWith("x-ontopia:cms:") && noOtherReference(occ)) store.remove(Integer.parseInt(uri.substring(14)));
            }
            LocatorIF loc = new URILocator("x-ontopia:cms:" + key);
            occ.setLocator(loc);
            TopicIF reifier = occ.getReifier();
            TopicIF filename = getTopicById(tm, "filename");
            OccurrenceIF occurrence = null;
            Iterator it = reifier.getOccurrences().iterator();
            while (it.hasNext()) {
                occ = (OccurrenceIF) it.next();
                if (occ.getType() != null && occ.getType().equals(filename)) {
                    occurrence = occ;
                    break;
                }
            }
            if (occurrence == null) {
                TopicMapBuilderIF builder = tm.getBuilder();
                occurrence = builder.makeOccurrence(reifier, filename, file.getFileName());
            } else {
                occurrence.setValue(file.getFileName());
            }
        } catch (ContentStoreException e) {
            throw new ActionRuntimeException(e);
        } catch (MalformedURLException e) {
            throw new ActionRuntimeException(e);
        } catch (java.io.IOException e) {
            throw new ActionRuntimeException("Error when saving file to content store", e);
        }
    }

    private TopicIF getTopicById(TopicMapIF topicmap, String id) {
        LocatorIF base = topicmap.getStore().getBaseAddress();
        LocatorIF srcloc = base.resolveAbsolute('#' + id);
        return (TopicIF) topicmap.getObjectByItemIdentifier(srcloc);
    }

    private boolean noOtherReference(OccurrenceIF occurrence) {
        try {
            QueryProcessorIF processor = QueryUtils.getQueryProcessor(occurrence.getTopicMap());
            QueryResultIF result = processor.execute("select $OBJ from " + "resource($OBJ, \"" + occurrence.getLocator().getAddress() + "\"), " + "$OBJ /= @" + occurrence.getObjectId() + "?");
            boolean other = result.next();
            result.close();
            return other;
        } catch (InvalidQueryException e) {
            throw new ActionRuntimeException(e);
        }
    }
}
