package org.identifylife.taxonomy.store.repository;

import java.util.List;
import org.identifylife.taxonomy.store.model.Taxon;

/**
 * @author mike
 * @author dbarnier
 */
public interface TaxonRepository extends Repository<Taxon> {

    Taxon getByUuid(String uuid);

    List<Taxon> getByName(String name);

    List<Taxon> getByHierarchy(String hierarchy);

    List<Taxon> getByName(String name, int page, int perPage);

    List<Taxon> getByNameAndHierarchy(String name, String hierarchy);

    List<Taxon> getByNameAndHierarchy(String name, String hierarchy, int page, int perPage);

    List<Taxon> getByParent(String uuid);

    List<Taxon> getByParent(String uuid, int page, int perPage);

    List<Taxon> getByParentAndDescribed(String uuid, boolean described);

    List<Taxon> getByParentAndDepth(String uuid, int depth);

    List<Taxon> getByParentOrderByChildren(String uuid);

    List<Taxon> getByMappedTo(String uuid);

    List<Taxon> getByMappedFrom(String uuid);
}
