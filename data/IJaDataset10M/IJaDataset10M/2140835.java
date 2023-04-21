package org.gbif.portal.dao.tag;

import java.util.List;
import org.gbif.portal.dto.tag.BiRelationTagDTO;
import org.gbif.portal.dto.tag.BooleanTag;
import org.gbif.portal.dto.tag.GeographicalCoverageTag;
import org.gbif.portal.dto.tag.NumberTag;
import org.gbif.portal.dto.tag.StringTag;
import org.gbif.portal.dto.tag.TemporalCoverageTag;

/**
 * DAO for tag access
 * 
 * @author dmartin
 */
public interface SimpleTagDAO {

    public List<GeographicalCoverageTag> retrieveGeographicalCoverageTagsForEntity(final int tagId, final long entityId);

    public List<TemporalCoverageTag> retrieveTemporalCoverageTagsForEntity(final int tagId, final long entityId);

    public List<BiRelationTagDTO> retrieveBiRelationTagsForEntity(final int tagId, final long toEntityId);

    public List<BooleanTag> retrieveBooleanTagsForEntity(final int tagId, final long entityId);

    public List<StringTag> retrieveStringTagsForEntity(final int tagId, final long entityId);

    public List<NumberTag> retrieveNumberTagsForEntity(final int tagId, final long entityId);
}
