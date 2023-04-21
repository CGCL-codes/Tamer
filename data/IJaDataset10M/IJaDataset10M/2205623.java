package com.gorillalogic.dal.model.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.model.*;

public class CommonModel implements Model {

    private CommonUniverse universe;

    private CommonPkgTable pkgTable;

    public CommonModel(CommonPkgTable pkgTable, CommonUniverse universe) throws AccessException {
        this.pkgTable = pkgTable;
        this.universe = universe;
        nameSpace = new CommonNameSpace(this, pkgTable);
        pkg = new CommonPkg(this, pkgTable);
        entity = new CommonEntity(this, pkgTable);
        enumeration = new CommonEnum(this, pkgTable);
        association = new CommonAssociation(this, pkgTable);
        field = new CommonField(this, pkgTable);
        participant = new CommonParticipant(this, pkgTable);
        annotation = new CommonAnnotation(this, pkgTable);
        method = new CommonMethod(this, pkgTable);
        trigger = new CommonTrigger(this, pkgTable);
        parameter = new CommonParameter(this, pkgTable);
        if (pkgTable.row(Principal.TYPE_PRINCIPAL, false) != null) {
            principal = new CommonPrincipal(this, pkgTable);
        }
    }

    public String getName() {
        return getSourcePkg().getWorld().getName();
    }

    public void setName(String name) throws OperationException {
        throw new UnsupportedException("CommonModel.setName()");
    }

    public CommonPkgTable getSourcePkg() {
        return pkgTable;
    }

    public Model getMetaModel() {
        World world = getSourcePkg().getWorld();
        return world.getUniverse().getModel();
    }

    public Universe getUniverse() {
        return universe;
    }

    private CommonNameSpace nameSpace;

    private CommonPkg pkg;

    private CommonEntity entity;

    private CommonPrincipal principal;

    private CommonEnum enumeration;

    private CommonAssociation association;

    private CommonField field;

    private CommonParticipant participant;

    private CommonAnnotation annotation;

    private CommonMethod method;

    private CommonTrigger trigger;

    private CommonParameter parameter;

    public NameSpace nameSpace() {
        return nameSpace;
    }

    public Pkg pkg() {
        return pkg;
    }

    public Entity entity() {
        return entity;
    }

    public Principal principal() {
        return principal;
    }

    public com.gorillalogic.dal.model.Enum enumeration() {
        return enumeration;
    }

    public Association association() {
        return association;
    }

    public Field field() {
        return field;
    }

    public Participant participant() {
        return participant;
    }

    public Annotation annotation() {
        return annotation;
    }

    public Method method() {
        return method;
    }

    public Trigger trigger() {
        return trigger;
    }

    public Parameter parameter() {
        return parameter;
    }

    public CommonNameSpace commonNameSpace() {
        return nameSpace;
    }

    public CommonPkg commonPkg() {
        return pkg;
    }

    public CommonEntity commonEntity() {
        return entity;
    }

    public CommonAssociation commonAssociation() {
        return association;
    }

    public CommonField commonField() {
        return field;
    }

    public CommonParticipant commonParticipant() {
        return participant;
    }

    public CommonAnnotation commonAnnotation() {
        return annotation;
    }

    public CommonMethod commonMethod() {
        return method;
    }

    public CommonParameter commonParameter() {
        return parameter;
    }

    public CommonTrigger commonTrigger() {
        return trigger;
    }

    public NameSpace.Itr nameSpaceItr() throws AccessException {
        return nameSpace().nameSpaceItr();
    }

    public Pkg.Itr pkgItr() throws AccessException {
        return pkg().pkgItr();
    }

    public Entity.Itr entityItr() throws AccessException {
        return entity().entityItr();
    }

    public Association.Itr associationItr() throws AccessException {
        return association().associationItr();
    }

    public Field.Itr fieldItr() throws AccessException {
        return field().fieldItr();
    }

    public Participant.Itr participantItr() throws AccessException {
        return participant().participantItr();
    }

    public Annotation.Itr annotationItr() throws AccessException {
        return annotation().annotationItr();
    }

    public Method.Itr methodItr() throws AccessException {
        return method().methodItr();
    }

    public Parameter.Itr parameterItr() throws AccessException {
        return parameter().parameterItr();
    }

    public Trigger.Itr triggerItr() throws AccessException {
        return trigger().triggerItr();
    }

    public NameSpace.Row findNameSpace(String nm) throws AccessException {
        return nameSpace().nameSpaceRow(nm);
    }

    public Pkg.Row findPkg(String nm) throws AccessException {
        return pkg().pkgRow(nm);
    }

    public Entity.Row findEntity(String nm) throws AccessException {
        return entity().entityRow(nm);
    }

    public Association.Row findAssociation(String nm) throws AccessException {
        return association().associationRow(nm);
    }

    public Field.Row findField(String nm) throws AccessException {
        return field().fieldRow(nm);
    }

    public Participant.Row findParticipant(String nm) throws AccessException {
        return participant().participantRow(nm);
    }

    public Annotation.Row findAnnotation(String nm) throws AccessException {
        return annotation().annotationRow(nm);
    }

    public void assignRoleNames() throws AccessException {
        ModelUtils.assignRoleNames(this);
    }

    public static void main(String[] argv) {
        Remain.go(argv);
    }
}
