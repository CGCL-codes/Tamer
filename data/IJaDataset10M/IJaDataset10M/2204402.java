package com.gnizr.web.view.freemarker;

import java.util.ArrayList;
import java.util.List;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import com.gnizr.core.web.junit.GnizrWebappTestBase;
import com.gnizr.db.dao.Tag;
import com.gnizr.db.dao.tag.TagDao;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.CollectionModel;
import freemarker.template.SimpleHash;

public class TestComputeTagCloudMethod extends GnizrWebappTestBase {

    private TagDao tagDao;

    protected void setUp() throws Exception {
        super.setUp();
        tagDao = getGnizrDao().getTagDao();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testExec() throws Exception {
        List<Tag> tags = tagDao.findTag(50, TagDao.SORT_ALPH);
        List<Object> args = new ArrayList<Object>();
        CollectionModel cm = new CollectionModel(tags, new BeansWrapper());
        args.add(cm);
        ComputeTagCloudMethod method = new ComputeTagCloudMethod();
        SimpleHash map = (SimpleHash) method.exec(args);
        assertEquals(6, map.size());
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSet(TestComputeTagCloudMethod.class.getResourceAsStream("/TestComputeTagCloudMethod-input.xml"));
    }
}
