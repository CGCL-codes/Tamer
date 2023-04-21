package org.vardb.sequences.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.vardb.CConstants;
import org.vardb.CVardbException;
import org.vardb.blast.CBlastHits;
import org.vardb.blast.CBlastHits.Hsp;
import org.vardb.lists.dao.CGroup;
import org.vardb.sequences.ISequenceView;

@MappedSuperclass
public abstract class CAbstractSequenceView extends CAbstractSequence implements ISequenceView {

    protected Integer id;

    protected String username;

    protected Disease disease;

    protected Country country;

    protected Source source;

    protected Chromosome chromosome;

    protected Family family;

    protected Genome genome;

    protected Ortholog ortholog;

    protected Paralog paralog;

    protected Pathogen pathogen;

    protected Ref ref;

    protected Sequenceset sequenceset;

    protected Subgroup subgroup;

    protected Taxon taxon;

    protected CGroup group;

    protected List<CTagData> tags = new ArrayList<CTagData>();

    protected String hits;

    protected String matches;

    protected Hit hit;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Embedded
    public Disease getDisease() {
        return this.disease;
    }

    public void setDisease(final Disease disease) {
        this.disease = disease;
    }

    @Embedded
    public Country getCountry() {
        return this.country;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    @Embedded
    public Source getSource() {
        return this.source;
    }

    public void setSource(final Source source) {
        this.source = source;
    }

    @Embedded
    public Chromosome getChromosome() {
        return this.chromosome;
    }

    public void setChromosome(final Chromosome chromosome) {
        this.chromosome = chromosome;
    }

    @Embedded
    public Family getFamily() {
        return this.family;
    }

    public void setFamily(final Family family) {
        this.family = family;
    }

    @Embedded
    public Genome getGenome() {
        return this.genome;
    }

    public void setGenome(final Genome genome) {
        this.genome = genome;
    }

    @Embedded
    public Ortholog getOrtholog() {
        return this.ortholog;
    }

    public void setOrtholog(final Ortholog ortholog) {
        this.ortholog = ortholog;
    }

    @Embedded
    public Paralog getParalog() {
        return this.paralog;
    }

    public void setParalog(final Paralog paralog) {
        this.paralog = paralog;
    }

    @Embedded
    public Pathogen getPathogen() {
        return this.pathogen;
    }

    public void setPathogen(final Pathogen pathogen) {
        this.pathogen = pathogen;
    }

    @Embedded
    public Ref getRef() {
        return this.ref;
    }

    public void setRef(final Ref ref) {
        this.ref = ref;
    }

    @Embedded
    public Sequenceset getSequenceset() {
        return this.sequenceset;
    }

    public void setSequenceset(final Sequenceset sequenceset) {
        this.sequenceset = sequenceset;
    }

    @Embedded
    public Subgroup getSubgroup() {
        return this.subgroup;
    }

    public void setSubgroup(final Subgroup subgroup) {
        this.subgroup = subgroup;
    }

    @Embedded
    public Taxon getTaxon() {
        return this.taxon;
    }

    public void setTaxon(final Taxon taxon) {
        this.taxon = taxon;
    }

    @Transient
    public CGroup getGroup() {
        return this.group;
    }

    public void setGroup(final CGroup group) {
        this.group = group;
    }

    @Transient
    public List<CTagData> getTags() {
        return this.tags;
    }

    public void addTags(Collection<CTagData> tags) {
        this.tags.addAll(tags);
    }

    @Transient
    public String getHits() {
        return this.hits;
    }

    public void setHits(final String hits) {
        this.hits = hits;
    }

    @Transient
    public String getMatches() {
        return this.matches;
    }

    public void setMatches(final String matches) {
        this.matches = matches;
    }

    @Transient
    public Hit getHit() {
        return this.hit;
    }

    public void setHit(final Hit hit) {
        this.hit = hit;
    }

    @Embeddable
    public static class Disease {

        protected Integer id;

        protected String name;

        protected String identifier;

        @Column(name = "disease_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "disease_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "disease_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Taxon {

        protected Integer id;

        protected String name;

        protected String identifier;

        @Column(name = "taxon_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "taxon_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "taxon_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Pathogen {

        protected Integer id;

        protected String name;

        protected String identifier;

        protected CConstants.PathogenType dtype;

        @Column(name = "pathogen_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "pathogen_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "pathogen_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }

        @Column(name = "pathogen_dtype")
        @Enumerated(EnumType.STRING)
        public CConstants.PathogenType getDtype() {
            return this.dtype;
        }

        public void setDtype(final CConstants.PathogenType dtype) {
            this.dtype = dtype;
        }
    }

    @Embeddable
    public static class Ortholog {

        protected Integer id;

        protected String name;

        protected String identifier;

        @Column(name = "ortholog_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "ortholog_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "ortholog_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Family {

        protected Integer id;

        protected String name;

        protected String identifier;

        protected String alias;

        @Column(name = "family_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "family_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "family_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }

        @Column(name = "family_alias")
        public String getAlias() {
            return this.alias;
        }

        public void setAlias(final String alias) {
            this.alias = alias;
        }
    }

    @Embeddable
    public static class Country {

        protected Integer id;

        protected String name;

        protected String identifier;

        protected String region;

        @Column(name = "country_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "country_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "country_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }

        @Column(name = "country_region")
        public String getRegion() {
            return this.region;
        }

        public void setRegion(final String region) {
            this.region = region;
        }
    }

    @Embeddable
    public static class Source {

        protected Integer id;

        protected String name;

        protected String identifier;

        protected String region;

        @Column(name = "source_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "source_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "source_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Subgroup {

        protected Integer id;

        protected String name;

        protected String identifier;

        @Column(name = "subgroup_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "subgroup_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "subgroup_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Genome {

        protected Integer id;

        protected String name;

        protected String identifier;

        @Column(name = "genome_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "genome_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "genome_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Chromosome {

        protected Integer id;

        protected String name;

        protected String identifier;

        @Column(name = "chromosome_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "chromosome_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "chromosome_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Sequenceset {

        protected Integer id;

        protected String identifier;

        @Column(name = "sequenceset_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "sequenceset_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Paralog {

        protected Integer id;

        protected String name;

        protected String identifier;

        @Column(name = "paralog_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "paralog_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "paralog_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    @Embeddable
    public static class Ref {

        protected Integer id;

        protected String name;

        protected String identifier;

        @Column(name = "ref_id")
        public Integer getId() {
            return this.id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        @Column(name = "ref_name")
        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Column(name = "ref_identifier")
        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }
    }

    public static class Hit {

        protected String chainname;

        protected Integer hitnumber;

        protected String hitid;

        protected String hitaccession;

        protected String hitdef;

        protected Integer hitlength;

        protected List<CBlastHits.Hsp> hsps = new ArrayList<CBlastHits.Hsp>();

        public String getChainname() {
            return this.chainname;
        }

        public void setChainname(final String chainname) {
            this.chainname = chainname;
        }

        public Integer getHitnumber() {
            return this.hitnumber;
        }

        public void setHitnumber(final Integer hitnumber) {
            this.hitnumber = hitnumber;
        }

        public String getHitid() {
            return this.hitid;
        }

        public void setHitid(final String hitid) {
            this.hitid = hitid;
        }

        public String getHitaccession() {
            return this.hitaccession;
        }

        public void setHitaccession(final String hitaccession) {
            this.hitaccession = hitaccession;
        }

        public String getHitdef() {
            return this.hitdef;
        }

        public void setHitdef(final String hitdef) {
            this.hitdef = hitdef;
        }

        public Integer getHitlength() {
            return this.hitlength;
        }

        public void setHitlength(final Integer hitlength) {
            this.hitlength = hitlength;
        }

        public List<CBlastHits.Hsp> getHsps() {
            return this.hsps;
        }

        public void setHsps(final List<CBlastHits.Hsp> hsps) {
            this.hsps = hsps;
        }

        public CBlastHits.Hsp getHsp() {
            if (this.hsps.isEmpty()) throw new CVardbException("BLAST: there should be 1 or more high-scoring pairs");
            return this.hsps.get(0);
        }

        public void addHsp(Hsp hsp) {
            this.hsps.add(hsp);
        }
    }
}
