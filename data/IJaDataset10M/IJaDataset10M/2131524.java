package com.netflexitysolutions.amazonws.sdb.internal.operations;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import com.amazonaws.sdb.doc._2009_04_15.DomainMetadata;
import com.amazonaws.sdb.doc._2009_04_15.DomainMetadataResponse;
import com.amazonaws.sdb.doc._2009_04_15.DomainMetadataResult;
import com.amazonaws.sdb.doc._2009_04_15.ResponseMetadata;
import com.netflexitysolutions.amazonws.sdb.Domain;
import com.netflexitysolutions.amazonws.sdb.SimpleDB;
import com.netflexitysolutions.amazonws.sdb.Statistics;

/**
 * @author netflexity
 *
 */
public class GetDomainMetadataOperation extends OperationExecutor<DomainMetadata, com.netflexitysolutions.amazonws.sdb.DomainMetadata> {

    private String domainName;

    /**
	 * @param simpleDB
	 * @param domain
	 * @param item
	 */
    public GetDomainMetadataOperation(SimpleDB simpleDB, Domain domain) {
        super(simpleDB);
        this.domainName = domain.getName();
        assert (StringUtils.isNotBlank(this.domainName));
    }

    @Override
    protected com.netflexitysolutions.amazonws.sdb.DomainMetadata call(DomainMetadata request) {
        com.netflexitysolutions.amazonws.sdb.DomainMetadata domainMetadata = null;
        request.setDomainName(domainName);
        DomainMetadataResponse response = getSimpleDB().getService().domainMetadata(request);
        if (response != null) {
            ResponseMetadata metadata = response.getResponseMetadata();
            if (metadata != null) {
                this.stats = new Statistics(metadata.getRequestId(), metadata.getBoxUsage());
            }
            DomainMetadataResult result = response.getDomainMetadataResult();
            if (result != null) {
                domainMetadata = new com.netflexitysolutions.amazonws.sdb.DomainMetadata(Integer.parseInt(result.getItemCount()), Integer.parseInt(result.getAttributeValueCount()), Integer.parseInt(result.getAttributeNameCount()), Long.parseLong(result.getItemNamesSizeBytes()), Long.parseLong(result.getAttributeValuesSizeBytes()), Long.parseLong(result.getAttributeNamesSizeBytes()), new Date(Long.parseLong(result.getTimestamp()) * 1000));
            }
        }
        return domainMetadata;
    }
}
