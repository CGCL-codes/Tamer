package org.jaffa.components.attachment.components.attachmentlookup.tx;

import java.util.*;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.persistence.UOW;
import org.jaffa.persistence.exceptions.UOWException;
import org.jaffa.persistence.Criteria;
import org.jaffa.persistence.AtomicCriteria;
import org.jaffa.components.finder.CriteriaField;
import org.jaffa.components.finder.OrderByField;
import org.jaffa.components.finder.FinderTx;
import org.jaffa.components.attachment.components.attachmentlookup.IAttachmentLookup;
import org.jaffa.components.attachment.components.attachmentlookup.dto.AttachmentLookupInDto;
import org.jaffa.components.attachment.components.attachmentlookup.dto.AttachmentLookupOutDto;
import org.jaffa.components.attachment.components.attachmentlookup.dto.AttachmentLookupOutRowDto;
import org.jaffa.components.attachment.domain.Attachment;
import org.jaffa.components.attachment.domain.AttachmentMeta;

/** Lookup for Attachment objects.
*/
public class AttachmentLookupTx implements IAttachmentLookup {

    private static Logger log = Logger.getLogger(AttachmentLookupTx.class);

    /**
     * This should be invoked, when done with the component.
     */
    public void destroy() {
    }

    /** Searches for Attachment objects.
     * @param input The criteria based on which the search will be performed.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error
     * @return The search results.
     */
    public AttachmentLookupOutDto find(AttachmentLookupInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Input: " + (input != null ? input.toString() : null));
            }
            uow = new UOW();
            Criteria criteria = buildCriteria(input, uow);
            Collection results = uow.query(criteria);
            AttachmentLookupOutDto output = buildDto(uow, results, input);
            if (log.isDebugEnabled()) {
                log.debug("Output: " + (output != null ? output.toString() : null));
            }
            return output;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    private Criteria buildCriteria(AttachmentLookupInDto input, UOW uow) {
        Criteria criteria = new Criteria();
        criteria.setTable(AttachmentMeta.getName());
        FinderTx.addCriteria(input.getAttachmentId(), AttachmentMeta.ATTACHMENT_ID, criteria);
        FinderTx.addCriteria(input.getSerializedKey(), AttachmentMeta.SERIALIZED_KEY, criteria);
        FinderTx.addCriteria(input.getOriginalFileName(), AttachmentMeta.ORIGINAL_FILE_NAME, criteria);
        FinderTx.addCriteria(input.getAttachmentType(), AttachmentMeta.ATTACHMENT_TYPE, criteria);
        FinderTx.addCriteria(input.getContentType(), AttachmentMeta.CONTENT_TYPE, criteria);
        FinderTx.addCriteria(input.getDescription(), AttachmentMeta.DESCRIPTION, criteria);
        FinderTx.addCriteria(input.getRemarks(), AttachmentMeta.REMARKS, criteria);
        FinderTx.addCriteria(input.getSupercededBy(), AttachmentMeta.SUPERCEDED_BY, criteria);
        FinderTx.addCriteria(input.getCreatedOn(), AttachmentMeta.CREATED_ON, criteria);
        FinderTx.addCriteria(input.getCreatedBy(), AttachmentMeta.CREATED_BY, criteria);
        FinderTx.addCriteria(input.getLastChangedOn(), AttachmentMeta.LAST_CHANGED_ON, criteria);
        FinderTx.addCriteria(input.getLastChangedBy(), AttachmentMeta.LAST_CHANGED_BY, criteria);
        OrderByField[] orderByFields = input.getOrderByFields();
        if (orderByFields != null) {
            for (int i = 0; i < orderByFields.length; i++) {
                OrderByField orderByField = orderByFields[i];
                int sort = Criteria.ORDER_BY_ASC;
                if (orderByField.getSortAscending() != null && !orderByField.getSortAscending().booleanValue()) sort = Criteria.ORDER_BY_DESC;
                criteria.addOrderBy(orderByField.getFieldName(), sort);
            }
        }
        return criteria;
    }

    private AttachmentLookupOutDto buildDto(UOW uow, Collection results, AttachmentLookupInDto input) throws UOWException {
        AttachmentLookupOutDto output = new AttachmentLookupOutDto();
        int maxRecords = input.getMaxRecords() != null ? input.getMaxRecords().intValue() : 0;
        int counter = 0;
        for (Iterator i = results.iterator(); i.hasNext(); ) {
            if (++counter > maxRecords && maxRecords > 0) {
                output.setMoreRecordsExist(Boolean.TRUE);
                break;
            }
            AttachmentLookupOutRowDto row = new AttachmentLookupOutRowDto();
            Attachment attachment = (Attachment) i.next();
            row.setAttachmentId(attachment.getAttachmentId());
            row.setSerializedKey(attachment.getSerializedKey());
            row.setOriginalFileName(attachment.getOriginalFileName());
            row.setAttachmentType(attachment.getAttachmentType());
            row.setContentType(attachment.getContentType());
            row.setDescription(attachment.getDescription());
            row.setRemarks(attachment.getRemarks());
            row.setSupercededBy(attachment.getSupercededBy());
            row.setCreatedOn(attachment.getCreatedOn());
            row.setCreatedBy(attachment.getCreatedBy());
            row.setLastChangedOn(attachment.getLastChangedOn());
            row.setLastChangedBy(attachment.getLastChangedBy());
            row.setData(attachment.getData());
            output.addRows(row);
        }
        return output;
    }
}
