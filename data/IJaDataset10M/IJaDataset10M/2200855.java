package org.mxeclipse.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import matrix.db.AttributeType;
import matrix.db.AttributeTypeItr;
import matrix.db.AttributeTypeList;
import matrix.db.BusinessType;
import matrix.db.BusinessTypeItr;
import matrix.db.BusinessTypeList;
import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.db.Relationship;
import matrix.db.RelationshipList;
import matrix.db.RelationshipType;
import matrix.db.RelationshipTypeItr;
import matrix.db.RelationshipTypeList;
import matrix.util.MatrixException;
import matrix.util.StringList;
import org.mxeclipse.business.table.type.MxTypeComposite;
import org.mxeclipse.configure.table.IMxTableColumnViewer;
import org.mxeclipse.exception.MxEclipseException;
import org.mxeclipse.utils.MxEclipseConstants;
import org.mxeclipse.utils.MxEclipseLogger;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.MqlUtil;

/**
 * <p>Title: MxTreeAttribute</p>
 * <p>Description: TODO class description?</p>
 * <p>Company: ABB Switzerland</p>
 * @author Tihomir Ilic
 * @version 1.0
 */
public class MxTreeRelationship extends MxTreeBusiness implements IAttributable, ITriggerable {

    public class DirectionInfo {

        private String cardinality;

        private String revision;

        private String clone;

        private boolean propagateModify;

        private boolean propagateConnection;

        public String getCardinality() {
            return cardinality;
        }

        public void setCardinality(String cardinality) {
            this.cardinality = cardinality;
        }

        public String getClone() {
            return clone;
        }

        public void setClone(String clone) {
            this.clone = clone;
        }

        public boolean isPropagateConnection() {
            return propagateConnection;
        }

        public void setPropagateConnection(boolean propagateConnection) {
            this.propagateConnection = propagateConnection;
        }

        public boolean isPropagateModify() {
            return propagateModify;
        }

        public void setPropagateModify(boolean propagateModify) {
            this.propagateModify = propagateModify;
        }

        public String getRevision() {
            return revision;
        }

        public void setRevision(String revision) {
            this.revision = revision;
        }
    }

    RelationshipType relationshipType;

    protected String description;

    protected boolean hidden;

    protected boolean preventDuplicates;

    protected ArrayList<MxTreeType> fromTypes;

    protected ArrayList<MxTreeType> toTypes;

    protected DirectionInfo fromInfo;

    protected DirectionInfo toInfo;

    protected static String MQL_INFO = "print relationship \"{0}\" select description hidden preventduplicates dump |;";

    protected static String MQL_DIRECTION_INFO = "print relationship \"{0}\" select {1}cardinality {1}reviseaction {1}cloneaction {1}propagatemodify {1}propagateconnection dump |;";

    protected static String MQL_ADD_ATTRIBUTE = "modify relationship \"{0}\" add attribute \"{1}\";";

    protected static String MQL_REMOVE_ATTRIBUTE = "modify relationship \"{0}\" remove attribute \"{1}\";";

    protected static String MQL_ADD_TYPES = "modify relationship \"{0}\" {1} add type \"{2}\";";

    protected static String MQL_REMOVE_TYPES = "modify relationship \"{0}\" {1} remove type \"{2}\";";

    protected static String MQL_MODIFY_DIRECTION_INFO = "modify relationship \"{0}\" {1} cardinality {2} revision {3} clone {4}  {5}  {6};";

    protected static final int INFO_DESCRIPTION = 0;

    protected static final int INFO_HIDDEN = 1;

    protected static final int INFO_PREVENT_DUPLICATES = 2;

    protected static final int DIRECTION_INFO_CARDINALITY = 0;

    protected static final int DIRECTION_INFO_REVISION = 1;

    protected static final int DIRECTION_INFO_CLONE = 2;

    protected static final int DIRECTION_INFO_PROPAGATE_MODIFY = 3;

    protected static final int DIRECTION_INFO_PROPAGATE_CONNECTION = 4;

    public static String[] CARDINALITIES = { "One", "N" };

    public static String[] REVISION_ACTIONS = { "none", "float", "replicate" };

    public static String[] CLONE_ACTIONS = { "none", "float", "replicate" };

    protected static ArrayList<MxTreeRelationship> allRelationships;

    /**
	 * TODO MxTreeAttribute constructor description?
	 * @param type
	 * @param name
	 * @throws MxEclipseException
	 * @throws MatrixException
	 */
    public MxTreeRelationship(String name) throws MxEclipseException, MatrixException {
        super(MxEclipseConstants.TYPE_RELATIONSHIP, name);
        this.relationshipType = new RelationshipType(name);
    }

    @Override
    public void refresh() throws MxEclipseException, MatrixException {
        super.refresh();
        this.relationshipType = new RelationshipType(name);
        this.fillBasics();
        this.attributes = getAttributes(true);
        this.fromTypes = getTypes(true, true);
        this.toTypes = getTypes(true, false);
        this.fillDirectionInfo(true);
        this.fillDirectionInfo(false);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isPreventDuplicates() {
        return preventDuplicates;
    }

    public void setPreventDuplicates(boolean preventDuplicates) {
        this.preventDuplicates = preventDuplicates;
    }

    public DirectionInfo getFromInfo() {
        return fromInfo;
    }

    public void setFromInfo(DirectionInfo fromInfo) {
        this.fromInfo = fromInfo;
    }

    public DirectionInfo getToInfo() {
        return toInfo;
    }

    public void setToInfo(DirectionInfo toInfo) {
        this.toInfo = toInfo;
    }

    public static ArrayList<MxTreeRelationship> getAllRelationships(boolean refresh) throws MatrixException, MxEclipseException {
        if (refresh || allRelationships == null) {
            Context context = getContext();
            RelationshipTypeList rtl = RelationshipType.getRelationshipTypes(context, true);
            allRelationships = new ArrayList<MxTreeRelationship>();
            RelationshipTypeItr rti = new RelationshipTypeItr(rtl);
            while (rti.next()) {
                RelationshipType rt = rti.obj();
                MxTreeRelationship rel = new MxTreeRelationship(rt.getName());
                allRelationships.add(rel);
            }
            Collections.sort(allRelationships);
        }
        return allRelationships;
    }

    public static String[] getAllRelationshipNames(boolean refresh) throws MatrixException, MxEclipseException {
        ArrayList<MxTreeRelationship> allRelationships = getAllRelationships(refresh);
        String[] retVal = new String[allRelationships.size()];
        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = allRelationships.get(i).getName();
        }
        return retVal;
    }

    public static ArrayList<MxTreeAttribute> getAttributes(MxTreeRelationship relationship) {
        ArrayList<MxTreeAttribute> retAttributes = new ArrayList<MxTreeAttribute>();
        try {
            Context context = getContext();
            relationship.relationshipType.open(context);
            try {
                Map mapAttributes = DomainRelationship.getTypeAttributes(context, relationship.getName(), true);
                for (Object oAttribute : mapAttributes.keySet()) {
                    MxTreeAttribute attribute = new MxTreeAttribute((String) oAttribute);
                    attribute.setParent(relationship);
                    attribute.setFrom(true);
                    attribute.setRelType(REL_TYPE_CONTAINS);
                    retAttributes.add(attribute);
                }
            } finally {
                relationship.relationshipType.close(context);
            }
        } catch (Exception ex) {
            MxEclipseLogger.getLogger().severe(ex.getMessage());
        }
        return retAttributes;
    }

    public ArrayList<MxTreeAttribute> getAttributes(boolean forceRefresh) {
        if (forceRefresh || attributes == null) {
            this.attributes = getAttributes(this);
        }
        return attributes;
    }

    /****************************************
	 * Type Related Stuff
	 * **************************************
	 */
    public void addType(boolean from) throws MxEclipseException, MatrixException {
        addType(new MxTreeType(""), from);
    }

    public void addType(MxTreeType newType, boolean from) {
        if (from) {
            this.fromTypes.add(newType);
        } else {
            this.toTypes.add(newType);
        }
        Iterator iterator = this.changeListeners.iterator();
        while (iterator.hasNext()) {
            IMxBusinessViewer contentProvider = (IMxBusinessViewer) iterator.next();
            if (contentProvider instanceof MxTypeComposite.MxTypeContentProvider) {
                if (from == ((MxTypeComposite.MxTypeContentProvider) contentProvider).getFrom()) {
                    contentProvider.addProperty(newType);
                }
            }
        }
    }

    public void removeType(MxTreeType type, boolean from) {
        if (from) {
            if (fromTypes == null) {
                getTypes(false, true);
            }
            this.fromTypes.remove(type);
        } else {
            if (toTypes == null) {
                getTypes(false, false);
            }
            this.toTypes.remove(type);
        }
        Iterator iterator = changeListeners.iterator();
        while (iterator.hasNext()) {
            IMxBusinessViewer contentProvider = (IMxBusinessViewer) iterator.next();
            if (contentProvider instanceof MxTypeComposite.MxTypeContentProvider) {
                if (from == ((MxTypeComposite.MxTypeContentProvider) contentProvider).getFrom()) {
                    contentProvider.removeProperty(type);
                }
            }
        }
    }

    public void fillBasics() {
        try {
            Context context = getContext();
            relationshipType.open(context);
            try {
                this.name = relationshipType.getName();
                MQLCommand command = new MQLCommand();
                command.executeCommand(context, MessageFormat.format(MQL_INFO, this.name));
                String[] info = command.getResult().trim().split("\\|");
                this.description = info[INFO_DESCRIPTION];
                this.hidden = info[INFO_HIDDEN].equalsIgnoreCase("true");
                this.preventDuplicates = info[INFO_PREVENT_DUPLICATES].equalsIgnoreCase("true");
            } finally {
                relationshipType.close(context);
            }
        } catch (Exception ex) {
            MxEclipseLogger.getLogger().severe(ex.getMessage());
        }
    }

    public static DirectionInfo getDirectionInfo(MxTreeRelationship relationship, boolean from) {
        DirectionInfo directionInfo = relationship.new DirectionInfo();
        try {
            Context context = getContext();
            MQLCommand command = new MQLCommand();
            command.executeCommand(context, MessageFormat.format(MQL_DIRECTION_INFO, relationship.getName(), from ? "from" : "to"));
            String[] info = command.getResult().trim().split("\\|");
            directionInfo.setCardinality(info[DIRECTION_INFO_CARDINALITY]);
            directionInfo.setRevision(info[DIRECTION_INFO_REVISION]);
            directionInfo.setClone(info[DIRECTION_INFO_CLONE]);
            directionInfo.setPropagateConnection(info[DIRECTION_INFO_PROPAGATE_CONNECTION].equalsIgnoreCase("true"));
            directionInfo.setPropagateModify(info[DIRECTION_INFO_PROPAGATE_MODIFY].equalsIgnoreCase("true"));
        } catch (Exception ex) {
            MxEclipseLogger.getLogger().severe(ex.getMessage());
        }
        return directionInfo;
    }

    public void fillDirectionInfo(boolean from) {
        DirectionInfo directionInfo = getDirectionInfo(this, from);
        if (from) {
            this.fromInfo = directionInfo;
        } else {
            this.toInfo = directionInfo;
        }
    }

    public static ArrayList<MxTreeType> getTypes(MxTreeRelationship relationship, boolean from) {
        ArrayList<MxTreeType> retTypes = new ArrayList<MxTreeType>();
        try {
            Context context = getContext();
            relationship.relationshipType.open(context);
            try {
                BusinessTypeList btl = (from ? relationship.relationshipType.getFromTypes(context) : relationship.relationshipType.getToTypes(context));
                BusinessTypeItr itBusiness = new BusinessTypeItr(btl);
                while (itBusiness.next()) {
                    BusinessType bt = itBusiness.obj();
                    MxTreeType child = new MxTreeType(bt.getName());
                    child.setParent(relationship);
                    child.setFrom(from);
                    child.setRelType(from ? REL_TYPE_FROM_TYPE : REL_TYPE_TO_TYPE);
                    retTypes.add(child);
                }
                for (MxTreeType child : retTypes) {
                    MxTreeType parentType = child.getParentType(false);
                    if (parentType != null) {
                        for (MxTreeType retType : retTypes) {
                            if (retType.getName().equals(parentType.getName())) {
                                child.setInherited(true);
                                break;
                            }
                        }
                    }
                }
            } finally {
                relationship.relationshipType.close(context);
            }
        } catch (Exception ex) {
            MxEclipseLogger.getLogger().severe(ex.getMessage());
        }
        return retTypes;
    }

    public ArrayList<MxTreeType> getTypes(boolean forceRefresh, boolean from) {
        if (from) {
            if (forceRefresh || this.fromTypes == null) {
                this.fromTypes = getTypes(this, from);
            }
            return this.fromTypes;
        } else {
            if (forceRefresh || this.toTypes == null) {
                this.toTypes = getTypes(this, from);
            }
            return this.toTypes;
        }
    }

    /**
	 * Saves attributes (add/remove) of the type to Matrix
	 * TODO if an attribute is replaced by another, make a state migration dialog/procedure/optional server script
	 * @param context
	 * @param command
	 * @throws MatrixException
	 */
    public void saveAttributes(Context context, MQLCommand command) throws MatrixException {
        ArrayList<MxTreeAttribute> oldAttributes = getAttributes(this);
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getOldName().equals("")) {
                command.executeCommand(context, MessageFormat.format(MQL_ADD_ATTRIBUTE, this.getName(), attributes.get(i).getName()));
            }
        }
        String mql = "";
        if (oldAttributes != null) {
            for (int i = 0; i < oldAttributes.size(); i++) {
                boolean bFound = false;
                MxTreeAttribute oldAttribute = oldAttributes.get(i);
                for (int j = 0; j < attributes.size(); j++) {
                    MxTreeAttribute attribute = attributes.get(j);
                    if (oldAttribute.getName().equals(attribute.getName())) {
                        bFound = true;
                        break;
                    } else if (oldAttribute.getName().equals(attribute.getOldName())) {
                        command.executeCommand(context, MessageFormat.format(MQL_ADD_ATTRIBUTE, this.getName(), attribute.getName()));
                        command.executeCommand(context, MessageFormat.format(MQL_REMOVE_ATTRIBUTE, this.getName(), oldAttribute.getOldName()));
                        bFound = true;
                        break;
                    }
                }
                if (!bFound) {
                    command.executeCommand(context, MessageFormat.format(MQL_REMOVE_ATTRIBUTE, this.getName(), oldAttribute.getOldName()));
                }
            }
        }
    }

    public void saveTypes(Context context, MQLCommand command, boolean from) throws MatrixException, MxEclipseException {
        ArrayList<MxTreeType> oldTypes = getTypes(this, from);
        ArrayList<MxTreeType> types = (from ? this.fromTypes : this.toTypes);
        String sAdded = "";
        String sRemoved = "";
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).getOldName().equals("")) {
                sAdded += (!sAdded.equals("") ? "," : "") + types.get(i).getName();
            }
        }
        if (oldTypes != null) {
            for (int i = 0; i < oldTypes.size(); i++) {
                boolean bFound = false;
                MxTreeType oldType = oldTypes.get(i);
                for (int j = 0; j < types.size(); j++) {
                    MxTreeType type = types.get(j);
                    if (oldType.getName().equals(type.getName())) {
                        bFound = true;
                        break;
                    } else if (oldType.getName().equals(type.getOldName())) {
                        sAdded += (!sAdded.equals("") ? "," : "") + type.getName();
                        sRemoved += (!sRemoved.equals("") ? "," : "") + oldType.getOldName();
                        bFound = true;
                        break;
                    }
                }
                if (!bFound) {
                    sRemoved += (!sRemoved.equals("") ? "," : "") + oldType.getOldName();
                }
            }
        }
        command.executeCommand(context, MessageFormat.format(MQL_ADD_TYPES, this.getName(), from ? MxTreeBusiness.REL_TYPE_FROM_TYPE : MxTreeBusiness.REL_TYPE_TO_TYPE, sAdded));
        command.executeCommand(context, MessageFormat.format(MQL_REMOVE_TYPES, this.getName(), from ? MxTreeBusiness.REL_TYPE_FROM_TYPE : MxTreeBusiness.REL_TYPE_TO_TYPE, sRemoved));
    }

    public void saveDirectionInfo(Context context, MQLCommand command, boolean from) throws MatrixException, MxEclipseException {
        DirectionInfo oldDirectionInfo = getDirectionInfo(this, from);
        DirectionInfo directionInfo = (from ? fromInfo : toInfo);
        command.executeCommand(context, MessageFormat.format(MQL_MODIFY_DIRECTION_INFO, this.getName(), from ? MxTreeBusiness.REL_TYPE_FROM_TYPE : MxTreeBusiness.REL_TYPE_TO_TYPE, directionInfo.getCardinality().equals("One") ? "1" : directionInfo.getCardinality(), directionInfo.getRevision(), directionInfo.getClone(), (directionInfo.isPropagateModify() != oldDirectionInfo.isPropagateModify() ? (directionInfo.isPropagateModify() ? "" : "!") + "propagatemodify" : ""), (directionInfo.isPropagateConnection() != oldDirectionInfo.isPropagateConnection() ? (directionInfo.isPropagateConnection() ? "" : "!") + "propagateconnection" : "")));
    }

    @Override
    public void save() {
        try {
            MQLCommand command = new MQLCommand();
            Context context = getContext();
            relationshipType.open(context);
            try {
                String modString = "";
                String relationshipName = relationshipType.getName();
                boolean changedName = !relationshipName.equals(this.getName());
                if (changedName) {
                    modString += " name \"" + this.getName() + "\"";
                }
                command.executeCommand(context, MessageFormat.format(MQL_INFO, relationshipType.getName()));
                String[] info = command.getResult().trim().split("\\|");
                ;
                if (!info[INFO_DESCRIPTION].equals(this.getDescription())) {
                    modString += " description \"" + this.getDescription() + "\"";
                }
                boolean oldIsHidden = info[INFO_HIDDEN].equalsIgnoreCase("true");
                if (oldIsHidden != this.isHidden()) {
                    modString += this.isHidden() ? " hidden" : " nothidden";
                }
                boolean oldPreventDuplicates = info[INFO_PREVENT_DUPLICATES].equalsIgnoreCase("true");
                if (oldPreventDuplicates != this.preventDuplicates) {
                    modString += this.preventDuplicates ? " preventduplicates" : " !preventduplicates";
                }
                if (!modString.equals("")) {
                    command.executeCommand(context, "modify relationship \"" + relationshipName + "\" " + modString + ";");
                }
                if (changedName) {
                    this.relationshipType = new RelationshipType(name);
                }
                saveAttributes(context, command);
                saveTypes(context, command, true);
                saveDirectionInfo(context, command, true);
                saveTypes(context, command, false);
                saveDirectionInfo(context, command, false);
                saveTriggers(context, command);
                allRelationships = null;
                attributes = null;
                fromTypes = null;
                toTypes = null;
                this.refresh();
            } finally {
                relationshipType.close(context);
            }
        } catch (Exception ex) {
            MxEclipseLogger.getLogger().severe(ex.getMessage());
        }
    }

    /**
	 * @return the children
	 */
    public MxTreeBusiness[] getChildren(boolean forceUpdate) throws MxEclipseException, MatrixException {
        if (forceUpdate) {
            children = null;
        }
        if (children == null) {
            children = new ArrayList<MxTreeBusiness>();
            children.addAll(this.getAttributes(false));
            children.addAll(this.getTypes(false, true));
            children.addAll(this.getTypes(false, false));
        }
        return (MxTreeBusiness[]) children.toArray(new MxTreeBusiness[children.size()]);
    }

    public static void clearCache() {
        allRelationships = null;
    }
}
