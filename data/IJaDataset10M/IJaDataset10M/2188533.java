package net.sourceforge.ondex.core.sql;

import net.sourceforge.ondex.core.*;
import net.sourceforge.ondex.core.sql.metadata.*;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SQLGraphMetaData extends SQLBase implements ONDEXGraphMetaData {

    private SQLGraph sg;

    private MetaDataFactory mdf;

    private HashMap<String, CV> cvs;

    private HashMap<String, EvidenceType> evidenceTypes;

    private HashMap<String, Unit> units;

    private HashMap<String, AttributeName> attribs;

    private HashMap<String, ConceptClass> conceptClasses;

    private HashMap<String, RelationType> relationTypes;

    public SQLGraphMetaData(int graphid, SQLGraph s) {
        super(graphid, s.getConnection());
        this.sg = s;
    }

    @Override
    public void associateGraph(ONDEXGraph g) throws AccessDeniedException {
        if (sid == -1L || sid == g.getSID()) {
            sid = (int) sg.getSID();
        } else {
            throw new AccessDeniedException("MetaData is already associated to another graph!");
        }
    }

    public void initialise() {
        this.mdf = new MetaDataFactory(this);
        this.evidenceTypes = new HashMap<String, EvidenceType>(2000);
        this.cvs = new HashMap<String, CV>(2000);
        this.units = new HashMap<String, Unit>(2000);
        this.attribs = new HashMap<String, AttributeName>(2000);
        this.conceptClasses = new HashMap<String, ConceptClass>(2000);
        this.relationTypes = new HashMap<String, RelationType>(2000);
    }

    @Override
    public MetaDataFactory getFactory() {
        return mdf;
    }

    @Override
    public CV getCV(String id) {
        return cvs.get(id);
    }

    @Override
    public boolean deleteCV(String id) {
        SQLCV s = (SQLCV) cvs.get(id);
        cvs.remove(id);
        s.die();
        return true;
    }

    @Override
    public boolean checkUnit(String id) {
        return units.containsKey(id);
    }

    @Override
    public boolean checkAttributeName(String id) {
        return attribs.containsKey(id);
    }

    @Override
    public boolean checkCV(String id) {
        return cvs.containsKey(id);
    }

    @Override
    public boolean checkEvidenceType(String id) {
        return evidenceTypes.containsKey(id);
    }

    @Override
    public boolean checkConceptClass(String id) {
        return conceptClasses.containsKey(id);
    }

    @Override
    public boolean checkRelationType(String id) {
        return relationTypes.containsKey(id);
    }

    @Override
    public Unit getUnit(String id) {
        return units.get(id);
    }

    @Override
    public Set<RelationType> getRelationTypes() {
        return new HashSet<RelationType>(relationTypes.values());
    }

    @Override
    public Set<Unit> getUnits() {
        return new HashSet<Unit>(units.values());
    }

    @Override
    public RelationType getRelationType(String id) {
        return relationTypes.get(id);
    }

    @Override
    public AttributeName getAttributeName(String id) {
        return attribs.get(id);
    }

    @Override
    public EvidenceType getEvidenceType(String id) {
        return evidenceTypes.get(id);
    }

    @Override
    public ConceptClass getConceptClass(String id) {
        return conceptClasses.get(id);
    }

    @Override
    public Set<AttributeName> getAttributeNames() {
        return new HashSet<AttributeName>(attribs.values());
    }

    @Override
    public Set<EvidenceType> getEvidenceTypes() {
        return new HashSet<EvidenceType>(evidenceTypes.values());
    }

    @Override
    public Set<ConceptClass> getConceptClasses() {
        return new HashSet<ConceptClass>(conceptClasses.values());
    }

    @Override
    public Set<CV> getCVs() {
        return new HashSet<CV>(cvs.values());
    }

    @Override
    public AttributeName createAttributeName(String id, String fullname, String description, Unit unit, Class<?> datatype, AttributeName specialisationOf) {
        SQLAttrName an = new SQLAttrName(sid, id, sg);
        an.initalise();
        an.setFullname(fullname);
        an.setDescription(description);
        if (unit != null) {
            an.setUnit(unit);
        }
        if (specialisationOf != null) {
            an.setSpecialisationOf(specialisationOf);
        }
        an.setClass(datatype);
        attribs.put(id, an);
        return an;
    }

    @Override
    public CV createCV(String id, String fullname, String description) {
        SQLCV cv = new SQLCV(sid, id, sg);
        cv.initalise();
        cv.setDescription(description);
        cv.setFullname(fullname);
        cvs.put(id, cv);
        return cv;
    }

    @Override
    public ConceptClass createConceptClass(String id, String fullname, String description, ConceptClass specialisationOf) {
        SQLConceptClass cc = new SQLConceptClass(sid, id, sg);
        cc.initalise();
        cc.setFullname(fullname);
        cc.setDescription(description);
        if (specialisationOf != null) {
            cc.setSpecialisationOf(specialisationOf);
        }
        conceptClasses.put(id, cc);
        return cc;
    }

    @Override
    public EvidenceType createEvidenceType(String id, String fullname, String description) {
        SQLEvidenceType et = new SQLEvidenceType(sid, id, sg);
        et.initalise();
        et.setFullname(fullname);
        et.setDescription(description);
        evidenceTypes.put(id, et);
        return et;
    }

    @Override
    public Unit createUnit(String id, String fullname, String description) {
        SQLUnit u = new SQLUnit(sid, id, sg);
        u.initalise();
        u.setDescription(description);
        u.setFullname(fullname);
        units.put(id, u);
        return u;
    }

    @Override
    public RelationType createRelationType(String id, String fullname, String description, String inverseName, boolean isAntisymmetric, boolean isReflexive, boolean isSymmetric, boolean isTransitiv, RelationType specialisationOf) {
        SQLRelationType rt = new SQLRelationType(sid, id, sg);
        rt.initalise();
        rt.setFullname(fullname);
        rt.setDescription(description);
        rt.setInverseName(inverseName);
        rt.setAntisymmetric(isAntisymmetric);
        rt.setReflexive(isReflexive);
        rt.setSymmetric(isSymmetric);
        rt.setTransitiv(isTransitiv);
        if (specialisationOf != null) {
            rt.setSpecialisationOf(specialisationOf);
        }
        relationTypes.put(id, rt);
        return rt;
    }

    @Override
    public boolean deleteAttributeName(String id) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean deleteConceptClass(String id) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean deleteEvidenceType(String id) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean deleteRelationType(String id) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean deleteUnit(String id) {
        throw new UnsupportedOperationException("todo");
    }
}
