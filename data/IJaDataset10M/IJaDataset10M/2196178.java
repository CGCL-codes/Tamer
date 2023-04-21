package com.clican.pluto.cms.core.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;
import org.jmock.Expectations;
import com.clican.pluto.cms.core.BaseTestCase;
import com.clican.pluto.cms.core.service.impl.DirectoryServiceImpl;
import com.clican.pluto.cms.dao.DirectoryDao;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.ITemplateDirectoryRelation;

public class DirectoryServiceTestCase extends BaseTestCase {

    private DirectoryServiceImpl directoryService;

    private ClassLoaderUtil classLoaderUtil;

    private DirectoryDao directoryDao;

    public void setDirectoryService(DirectoryService directoryService) {
        this.directoryService = (DirectoryServiceImpl) directoryService;
        classLoaderUtil = this.context.mock(ClassLoaderUtil.class);
        directoryDao = this.context.mock(DirectoryDao.class);
        this.directoryService.setClassLoaderUtil(classLoaderUtil);
        this.directoryService.setDirectoryDao(directoryDao);
    }

    public void testAppendDirectory() throws Exception {
        final String name = "root";
        final IDirectory result = new IDirectory() {

            private static final long serialVersionUID = 4645740417113237029L;

            public Set<IDirectory> getChildren() {
                return null;
            }

            public Calendar getCreateTime() {
                return null;
            }

            public Long getId() {
                return null;
            }

            public String getName() {
                return null;
            }

            public IDirectory getParent() {
                return null;
            }

            public String getPath() {
                return null;
            }

            public IDirectory getReference() {
                return null;
            }

            public Set<ITemplateDirectoryRelation> getTemplateDirectoryRelationSet() {
                return null;
            }

            public Calendar getUpdateTime() {
                return null;
            }

            public void setChildren(Set<IDirectory> children) {
            }

            public void setCreateTime(Calendar createTime) {
            }

            public void setId(Long id) {
            }

            public void setName(String name) {
            }

            public void setParent(IDirectory parent) {
            }

            public void setPath(String path) {
            }

            public void setReference(IDirectory reference) {
            }

            public void setTemplateDirectoryRelationSet(Set<ITemplateDirectoryRelation> templateDirectoryRelationSet) {
            }

            public void setUpdateTime(Calendar updateTime) {
            }
        };
        context.checking(new Expectations() {

            {
                one(classLoaderUtil).newDirectory(null, name);
                result.setPath(name);
                will(returnValue(result));
                one(directoryDao).save(result);
            }
        });
        Serializable directory = directoryService.appendDirectory(null, name);
        assertNotNull(directory);
    }
}
