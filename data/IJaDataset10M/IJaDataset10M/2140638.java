package com.vangent.hieos.xwebtools.servlets.xviewer;

import com.vangent.hieos.xwebtools.servlets.framework.HttpUtils;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;

/**
 *
 * @author NIST (adapted)
 */
public class FindDocsQueryContents extends PagedQueryContents {

    /**
     *
     * @param q_cntl
     * @param index
     * @param h
     * @param xv
     * @param cntl
     * @throws MetadataValidationException
     * @throws MetadataException
     */
    public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, XView xv, String cntl) throws MetadataValidationException, MetadataException {
        System.out.println("FindDocs QueryContents");
        Metadata m = getMetadata();
        boolean object_refs = m.isObjectRefsOnly();
        if (m.getWrapper() != null) {
            xv.getMetadata().addMetadata(m);
            this.displayStructureHeader(index, xv, this.initial_evidence);
        } else {
            this.displayStructureHeader(index, xv, this.initial_evidence);
            return;
        }
        if (object_refs) {
            h.indent1(this.size() + " elements " + xv.build_next_page_link(String.valueOf(index)));
        } else {
            for (String id : m.getExtrinsicObjectIds()) {
                h.indent1(xv.build_details_link(id, cntl) + " (" + xv.build_xml_link(id, cntl) + ")" + " {" + ((m.isRetrievable_a(id)) ? xv.build_ret_a_link(id) + " " : "") + ((m.isRetrievable_b(id)) ? xv.build_ret_b_link(id) + " " : "") + ((q_cntl.hasEndpoint()) ? xv.build_related_link(id) : "") + " " + ((q_cntl.hasEndpoint()) ? xv.build_ss_link(id) : "") + "}");
            }
        }
    }
}
