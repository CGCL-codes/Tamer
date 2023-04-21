package edu.unibi.agbi.dawismd.database.detailqueries.enzyme;

import java.util.ArrayList;
import java.util.HashSet;
import edu.unibi.agbi.dawismd.database.detailqueries.AbstractDetailQueries;
import edu.unibi.agbi.dawismd.entities.biodwh.kegg.ligand.enzyme.KeggEnzyme;
import edu.unibi.agbi.dawismd.entities.biodwh.kegg.ligand.enzyme.KeggEnzymeDblinks;
import edu.unibi.agbi.dawismd.entities.biodwh.kegg.ligand.enzyme.KeggEnzymeReference;
import edu.unibi.agbi.dawismd.entities.biodwh.uniprot.UniprotPdb;
import edu.unibi.agbi.dawismd.util.objects.Dblinks;
import edu.unibi.agbi.dawismd.util.objects.Reference;

public class EnzymeKegg extends AbstractDetailQueries {

    private KeggEnzyme enzyme;

    private ArrayList<String> name;

    private ArrayList<String> structures = new ArrayList<String>();

    private ArrayList<String> enzyme_class;

    private ArrayList<String> pathway;

    private ArrayList<String> compound;

    private ArrayList<String> glycan;

    private ArrayList<String> reaction;

    private ArrayList<String> gene_ontology;

    private ArrayList<String> gene;

    private ArrayList<String> protein;

    private ArrayList<String> scop;

    private ArrayList<String> substrate;

    private ArrayList<String> product;

    private ArrayList<String> cofactor;

    private ArrayList<String> orthology;

    private ArrayList<String> inhibitor;

    private ArrayList<String> effector;

    private ArrayList<Dblinks> db_links = new ArrayList<Dblinks>();

    private ArrayList<Reference> reference = new ArrayList<Reference>();

    public void setId(String id) {
        searchEnzyme(id);
        if (DATABASE_IDENTIFIER) {
            searchCompound(id);
            searchProtein(id);
            searchGene(id);
            searchName(id);
            searchDbLinks(id);
            searchEnzymeClass(id);
            searchPathway(id);
            searchReaction(id);
            searchScop(id);
            searchSubstrate(id);
            searchProduct(id);
            searchCofactor(id);
            searchInhibitor(id);
            searchEffector(id);
            searchOrthology(id);
            searchGlycan(id);
            searchReference(id);
            searchGeneOntology(id);
        }
        SESSION.close();
    }

    private void searchEnzyme(String id) {
        enzyme = (KeggEnzyme) SESSION.createQuery("from kegg_enzyme a where a.entry = :id").setParameter("id", id).uniqueResult();
        if (enzyme != null) {
            DATABASE_IDENTIFIER = true;
        }
    }

    @SuppressWarnings("unchecked")
    private void searchName(String id) {
        name = (ArrayList<String>) SESSION.createQuery("select a.name from kegg_enzyme_name a where a.keggEnzyme.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchStructures(String id) {
        ArrayList<UniprotPdb> tmp = (ArrayList<UniprotPdb>) SESSION.createQuery("select a.uniprot.uniprotPdbs from uniprot_dblinks a where a.dbname = :db and a.identifier = :id").setParameter("db", "BRENDA").setParameter("id", id).list();
        if (tmp != null) {
            if (!tmp.isEmpty()) {
                for (UniprotPdb obj : tmp) {
                    structures.add(obj.getId().getPdbId());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void searchEnzymeClass(String id) {
        enzyme_class = (ArrayList<String>) SESSION.createQuery("select a.id.enzymeClass from kegg_enzyme_class a where a.id.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchPathway(String id) {
        pathway = (ArrayList<String>) SESSION.createQuery("select concat(a.id.org, a.id.number) from kegg_enzyme_pathway a where a.id.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchDbLinks(String id) {
        ArrayList<KeggEnzymeDblinks> db_list = (ArrayList<KeggEnzymeDblinks>) SESSION.createQuery("from kegg_enzyme_dblinks a where a.id.dbname != :db and a.id.entry = :id").setParameter("db", "BRENDA, the Enzyme Database").setParameter("id", id).list();
        for (KeggEnzymeDblinks db : db_list) {
            db_links.add(new Dblinks(db.getId().getDbname(), db.getId().getIdentifier()));
        }
    }

    @SuppressWarnings("unchecked")
    private void searchReference(String id) {
        ArrayList<KeggEnzymeReference> ref_list = (ArrayList<KeggEnzymeReference>) SESSION.createQuery("from kegg_enzyme_reference a where a.id.entry = :id").setParameter("id", id).list();
        for (KeggEnzymeReference ref : ref_list) {
            reference.add(new Reference(ref.getAuthors(), ref.getTitle(), ref.getJournal(), ref.getId().getPubmedId()));
        }
    }

    @SuppressWarnings("unchecked")
    private void searchGlycan(String id) {
        glycan = (ArrayList<String>) SESSION.createQuery("select a.id.entry from kegg_glycan_enzyme a where a.id.enzyme = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchCompound(String id) {
        compound = (ArrayList<String>) SESSION.createQuery("select a.id.entry from kegg_compound_enzyme a where a.id.enzyme = :id").setParameter("id", id).list();
        compound.addAll(SESSION.createQuery("select a.moleculeId from tp_molecule a join a.externalDatabaseLinkId b where b.database = :db and b.databaseIdentifier = :id").setParameter("db", "BRENDA").setParameter("id", id).list());
    }

    @SuppressWarnings("unchecked")
    private void searchReaction(String id) {
        reaction = (ArrayList<String>) SESSION.createQuery("select a.id.entry from kegg_reaction_enzyme a where a.id.enzyme = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchGeneOntology(String id) {
        HashSet<String> set = new HashSet<String>(SESSION.createQuery("select b.acc from go_term_dbxref a join a.term b where a.dbxref.xrefDbname = :db and a.dbxref.xrefKey = :id").setParameter("db", "EC").setParameter("id", id).list());
        set.addAll(SESSION.createQuery("select b.acc from go_term_dbxref a join a.term b where a.dbxref.xrefDbname = :db and a.dbxref.xrefKey = :id").setParameter("db", "BRENDA").setParameter("id", id).list());
        gene_ontology = new ArrayList<String>(set);
    }

    @SuppressWarnings("unchecked")
    private void searchGene(String id) {
        gene = (ArrayList<String>) SESSION.createQuery("select a.id.geneId from kegg_enzyme_genes a where a.id.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchScop(String id) {
        searchStructures(id);
        if (structures != null) {
            if (!structures.isEmpty()) {
                HashSet<String> list = new HashSet<String>();
                for (String pdb : structures) {
                    pdb = pdb.toLowerCase();
                    list.addAll(SESSION.createQuery("select a.sccs from scop_domain a where a.pdbId = :pdb").setParameter("pdb", pdb).list());
                }
                scop = new ArrayList<String>(list);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void searchProtein(String id) {
        HashSet<String> temp = new HashSet<String>();
        temp.addAll(SESSION.createQuery("select a.id.entryName from enzyme_uniprot a where a.id.ecNumber = :id").setParameter("id", id).list());
        temp.addAll(SESSION.createQuery("select a.uniprot.uniprotId from uniprot_dblinks a where a.dbname = :dbname and a.identifier = :id").setParameter("dbname", "BRENDA").setParameter("id", id).list());
        protein = new ArrayList<String>(temp);
    }

    @SuppressWarnings("unchecked")
    private void searchSubstrate(String id) {
        substrate = (ArrayList<String>) SESSION.createQuery("select a.id.substrate from kegg_enzyme_substrate a where a.id.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchProduct(String id) {
        product = (ArrayList<String>) SESSION.createQuery("select a.id.product from kegg_enzyme_product a where a.id.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchCofactor(String id) {
        cofactor = (ArrayList<String>) SESSION.createQuery("select a.id.cofactor from kegg_enzyme_cofactor a where a.id.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchInhibitor(String id) {
        inhibitor = (ArrayList<String>) SESSION.createQuery("select a.id.inhibitor from kegg_enzyme_inhibitor a where a.id.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchEffector(String id) {
        effector = (ArrayList<String>) SESSION.createQuery("select a.id.effector from kegg_enzyme_effector a where a.id.entry = :id").setParameter("id", id).list();
    }

    @SuppressWarnings("unchecked")
    private void searchOrthology(String id) {
        orthology = (ArrayList<String>) SESSION.createQuery("select a.id.orthology from kegg_enzyme_orthology a where a.id.entry = :id").setParameter("id", id).list();
    }

    public KeggEnzyme getEnzyme() {
        return enzyme;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<String> getStructures() {
        return structures;
    }

    public ArrayList<String> getEnzymeClass() {
        return enzyme_class;
    }

    public ArrayList<String> getPathway() {
        return pathway;
    }

    public ArrayList<String> getCompound() {
        return compound;
    }

    public ArrayList<String> getGlycan() {
        return glycan;
    }

    public ArrayList<String> getReaction() {
        return reaction;
    }

    public ArrayList<String> getGeneOntology() {
        return gene_ontology;
    }

    public ArrayList<String> getGene() {
        return gene;
    }

    public ArrayList<String> getProtein() {
        return protein;
    }

    public ArrayList<String> getScop() {
        return scop;
    }

    public ArrayList<Dblinks> getDbLinks() {
        return db_links;
    }

    public ArrayList<Reference> getReference() {
        return reference;
    }

    public ArrayList<String> getSubstrate() {
        return substrate;
    }

    public ArrayList<String> getProduct() {
        return product;
    }

    public ArrayList<String> getCofactor() {
        return cofactor;
    }

    public ArrayList<String> getOrthology() {
        return orthology;
    }

    public ArrayList<String> getInhibitor() {
        return inhibitor;
    }

    public ArrayList<String> getEffector() {
        return effector;
    }
}
