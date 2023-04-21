package com.c2b2.ipoint.model.test.casemanagement;

import com.c2b2.ipoint.model.Blog;
import com.c2b2.ipoint.model.Code;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.User;
import com.c2b2.ipoint.model.casemanagement.Case;
import com.c2b2.ipoint.presentation.PresentationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.hibernate.HibernateException;

/**
  * $Id: CaseTestCase.java,v 1.3 2007/07/03 15:47:37 steve Exp $
  *
  * Copyright 2006 C2B2 Consulting Limited. All rights reserved.
  * Use of this code is subject to license.
  * Please check your license agreement for usage restrictions
  *
  * This class .
  *
  * @author $Author: steve $
  * @version $Revision: 1.3 $
  * $Date: 2007/07/03 15:47:37 $
  *
  */
public class CaseTestCase extends TestCase {

    private static int numadded;

    public CaseTestCase() {
        try {
            CaseTestCase.numadded = Case.findAll().size();
        } catch (PersistentModelException pme) {
            pme.printStackTrace();
        }
    }

    public void testCreate() throws PersistentModelException {
        String caseName = "Test Case";
        String caseDesc = "Case description for the test case";
        User caseEngineer = User.findUser(2);
        User caseReporter = User.findUser(2);
        Code caseStatus = Code.find("Case_Status", "New");
        Code caseType = Code.find("Case_Type", "Bug");
        Code casePriority = Code.find("Case_Priority", "Normal");
        Date caseDate = new Date();
        Blog caseBlog = Blog.createBlog(caseReporter);
        Case test = Case.createCase(caseName, caseDesc, caseEngineer, caseReporter, caseDate, caseStatus, caseBlog, casePriority, caseType);
        CaseTestCase.numadded++;
        assertNotNull(test);
        assertEquals(test.getTitle(), caseName);
        assertEquals(test.getDescription(), caseDesc);
        assertEquals(test.getCaseBlog(), caseBlog);
        assertEquals(test.getEngineer(), caseEngineer);
        assertEquals(test.getReporter(), caseReporter);
        assertEquals(test.getDateRaised(), caseDate);
        assertEquals(test.getPriority(), casePriority);
        assertEquals(test.getStatus(), caseStatus);
        assertEquals(test.getCaseType(), caseType);
    }

    public void testFind() throws PersistentModelException {
        String caseName = "Test Case 2";
        String caseDesc = "Case description for the second test case";
        User caseEngineer = User.findUser(2);
        User caseReporter = User.findUser(2);
        Code caseStatus = Code.find("Case_Status", "New");
        Code caseType = Code.find("Case_Type", "Bug");
        Code casePriority = Code.find("Case_Priority", "Normal");
        Date caseDate = new Date();
        Blog caseBlog = Blog.createBlog(caseReporter);
        Case test = Case.createCase(caseName, caseDesc, caseEngineer, caseReporter, caseDate, caseStatus, caseBlog, casePriority, caseType);
        CaseTestCase.numadded++;
        long createdId = test.getID();
        Case test2 = Case.findCase(createdId);
        assertEquals(test2.getTitle(), test.getTitle());
    }

    public void testFindCasesForUser() throws PersistentModelException {
        String caseName = "Test Case 3";
        String caseDesc = "Case description for the third test case";
        User caseEngineer = User.findUser(1);
        User caseReporter = User.findUser(1);
        Code caseStatus = Code.find("Case_Status", "New");
        Code caseType = Code.find("Case_Type", "Bug");
        Code casePriority = Code.find("Case_Priority", "Normal");
        Date caseDate = new Date();
        Blog caseBlog = Blog.createBlog(caseReporter);
        Case test = Case.createCase(caseName, caseDesc, caseEngineer, caseReporter, caseDate, caseStatus, caseBlog, casePriority, caseType);
        CaseTestCase.numadded++;
        caseName = "Test Case 4";
        caseDesc = "Case description for the fourth test case";
        caseEngineer = User.findUser(2);
        caseReporter = User.findUser(2);
        caseStatus = Code.find("Case_Status", "New");
        caseType = Code.find("Case_Type", "Bug");
        casePriority = Code.find("Case_Priority", "Normal");
        caseDate = new Date();
        caseBlog = Blog.createBlog(caseReporter);
        Case test2 = Case.createCase(caseName, caseDesc, caseEngineer, caseReporter, caseDate, caseStatus, caseBlog, casePriority, caseType);
        CaseTestCase.numadded++;
        List<Case> testresults = Case.findCasesForUser(User.findUser(1).getUserId());
        Iterator<Case> it = testresults.iterator();
        Case testResult = it.next();
        assertEquals(test.getID(), testResult.getID());
    }

    public void testFindAll() throws PersistentModelException {
        List<Case> results = Case.findAll();
        assertEquals(results.size(), CaseTestCase.numadded);
    }

    public void testDelete() throws PersistentModelException {
        String caseName = "Test Case 5";
        String caseDesc = "Case description for the fifth test case";
        User caseEngineer = User.findUser(2);
        User caseReporter = User.findUser(2);
        Code caseStatus = Code.find("Case_Status", "New");
        Code caseType = Code.find("Case_Type", "Bug");
        Code casePriority = Code.find("Case_Priority", "Normal");
        Date caseDate = new Date();
        Blog caseBlog = Blog.createBlog(caseReporter);
        Case test = Case.createCase(caseName, caseDesc, caseEngineer, caseReporter, caseDate, caseStatus, caseBlog, casePriority, caseType);
        CaseTestCase.numadded++;
        assertNotNull(test);
        long caseId = test.getID();
        Case.delete(test);
        CaseTestCase.numadded--;
        try {
            Case caseFound = Case.findCase(caseId);
            assertNull(caseFound);
        } catch (PersistentModelException he) {
        }
    }
}
