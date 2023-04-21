package net.lunglet.hdf;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.util.HashSet;
import java.util.Set;
import net.lunglet.hdf.H5Library.H5G_iterate_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Group extends H5Object {

    private static final CloseAction CLOSE_ACTION = new CloseAction() {

        @Override
        public void close(final int id) {
            int err = H5Library.INSTANCE.H5Gclose(id);
            if (err < 0) {
                throw new H5GroupException("H5Gclose failed");
            }
        }
    };

    private final Logger logger = LoggerFactory.getLogger(Group.class);

    Group(final int id, final boolean isRootGroup) {
        super(id, isRootGroup ? null : CLOSE_ACTION);
        logger.debug("Created {} [id={}]", getName(), getId());
    }

    public DataSet createDataSet(final String name, final DataType dataType, final DataSpace dataSpace) {
        return createDataSet(name, dataType, dataSpace, DataSetCreatePropList.DEFAULT);
    }

    public DataSet createDataSet(final String name, final DataType dataType, final DataSpace dataSpace, final DataSetCreatePropList createPlist) {
        int typeId = dataType.getId();
        int spaceId = dataSpace.getId();
        int createPlistId = createPlist.getId();
        final int datasetId;
        synchronized (H5Library.INSTANCE) {
            datasetId = H5Library.INSTANCE.H5Dcreate(getId(), name, typeId, spaceId, createPlistId);
            if (datasetId < 0) {
                throw new H5GroupException("H5Dcreate of " + name + " failed", true);
            }
        }
        return new DataSet(datasetId);
    }

    public DataSet createDataSet(final String name, final DataType dataType, final long... dims) {
        DataSpace space = new DataSpace(dims);
        try {
            DataSet dataset = createDataSet(name, dataType, space);
            return dataset;
        } finally {
            space.close();
        }
    }

    public Group createGroup(final String name) {
        final int groupId;
        synchronized (H5Library.INSTANCE) {
            groupId = H5Library.INSTANCE.H5Gcreate(getId(), name, new NativeLong(0));
            if (groupId < 0) {
                throw new H5GroupException("H5Gcreate of " + name + " failed");
            }
        }
        return new Group(groupId, false);
    }

    public boolean existsDataSet(final String name) {
        final int datasetId;
        synchronized (H5Library.INSTANCE) {
            datasetId = H5Library.INSTANCE.H5Dopen(getId(), name);
        }
        if (datasetId < 0) {
            return false;
        }
        new DataSet(datasetId).close();
        return true;
    }

    public boolean existsGroup(final String name) {
        final int groupId;
        synchronized (H5Library.INSTANCE) {
            groupId = H5Library.INSTANCE.H5Gopen(getId(), name);
        }
        if (groupId < 0) {
            return false;
        }
        new Group(groupId, false).close();
        return true;
    }

    public Set<String> getDataSetNames() {
        Set<String> names = new HashSet<String>();
        for (DataSet dataset : getDataSets()) {
            names.add(dataset.getName());
            dataset.close();
        }
        return names;
    }

    public Set<DataSet> getDataSets() {
        logger.debug("Getting datasets for {} [id={}]", getName(), getId());
        final IntByReference idx = new IntByReference(0);
        final Set<String> datasetNames = new HashSet<String>();
        final H5G_iterate_t operator = new H5G_iterate_t() {

            @Override
            public int callback(final int locId, final String datasetName, final Pointer data) {
                int type = H5Library.INSTANCE.H5Gget_objtype_by_idx(locId, idx.getValue());
                if (type == H5Library.H5G_DATASET) {
                    datasetNames.add(datasetName);
                }
                idx.setValue(idx.getValue() + 1);
                return 0;
            }
        };
        synchronized (H5Library.INSTANCE) {
            H5Library.INSTANCE.H5Giterate(getId(), ".", idx, operator, null);
        }
        HashSet<DataSet> datasets = new HashSet<DataSet>();
        for (String datasetName : datasetNames) {
            datasets.add(openDataSet(datasetName));
        }
        return datasets;
    }

    public Set<Group> getGroups() {
        logger.debug("Getting groups for {} [id={}]", getName(), getId());
        final IntByReference idx = new IntByReference(0);
        final Set<String> groupNames = new HashSet<String>();
        final H5G_iterate_t operator = new H5G_iterate_t() {

            @Override
            public int callback(final int locId, final String groupName, final Pointer data) {
                int type = H5Library.INSTANCE.H5Gget_objtype_by_idx(locId, idx.getValue());
                if (type == H5Library.H5G_GROUP) {
                    groupNames.add(groupName);
                }
                idx.setValue(idx.getValue() + 1);
                return 0;
            }
        };
        synchronized (H5Library.INSTANCE) {
            H5Library.INSTANCE.H5Giterate(getId(), ".", idx, operator, null);
        }
        HashSet<Group> groups = new HashSet<Group>();
        for (String groupName : groupNames) {
            groups.add(openGroup(groupName));
        }
        return groups;
    }

    public DataSet openDataSet(final String name) {
        final int datasetId;
        synchronized (H5Library.INSTANCE) {
            datasetId = H5Library.INSTANCE.H5Dopen(getId(), name);
            if (datasetId < 0) {
                throw new H5GroupException("H5Dopen of " + name + " failed", true);
            }
        }
        return new DataSet(datasetId);
    }

    public Group openGroup(final String name) {
        logger.debug("Opening group {}", name);
        final int groupId;
        synchronized (H5Library.INSTANCE) {
            groupId = H5Library.INSTANCE.H5Gopen(getId(), name);
            if (groupId < 0) {
                throw new H5GroupException("H5Gopen of " + name + " failed", true);
            }
        }
        return new Group(groupId, false);
    }
}
