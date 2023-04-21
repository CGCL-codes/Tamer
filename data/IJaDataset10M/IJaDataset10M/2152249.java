package com.webapp.db.JDBC;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.webapp.db.factory.DBProvider;
import com.webapp.models.Comment;
import com.webapp.models.Company;
import com.webapp.models.Discussion;
import com.webapp.models.Man;
import com.webapp.models.Problem;
import com.webapp.models.Service;

public class JDBCProvider implements DBProvider {

    private static JDBCProvider instance = new JDBCProvider();

    private JDBCProvider() {
    }

    public static JDBCProvider getInstance() {
        return instance;
    }

    @Override
    public Man getManByLoginAndPassword(String login, String password) throws Exception {
        final String sql = SQLQuerryConstants.GET_MAN_BY_LIGIN_AND_PASSWORD;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, login, password);
        if (rs.next()) {
            final Man man = new Man(rs);
            rs.close();
            return man;
        } else {
            rs.getStatement().close();
            return null;
        }
    }

    @Override
    public void addMan(String firstName, String lastName, String login, String password) throws Exception {
        final String sql = SQLQuerryConstants.ADD_MAN;
        JDBCUtil.getInstance().executeSQL(sql, firstName, lastName, login, password);
    }

    @Override
    public List<Comment> getCommentsByDiscussionId(int discussionID) throws Exception {
        final List<Comment> comments = new ArrayList<Comment>();
        final String sql = SQLQuerryConstants.GET_COMMENTS_BY_DISCUSSION_ID;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, discussionID);
        while (rs.next()) {
            Comment comment = new Comment(rs);
            comments.add(comment);
        }
        rs.getStatement().close();
        rs.close();
        return comments;
    }

    @Override
    public void addComment(int discussion_id, String text) throws Exception {
        final String sql = SQLQuerryConstants.ADD_COMMENT;
        JDBCUtil.getInstance().executeSQL(sql, discussion_id, text);
    }

    @Override
    public List<Company> getAllCompanies() throws Exception {
        final List<Company> companies = new ArrayList<Company>();
        final String sql = SQLQuerryConstants.GET_ALL_COMPANIES;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql);
        while (rs.next()) {
            Company company = new Company(rs);
            companies.add(company);
        }
        rs.getStatement().close();
        rs.close();
        return companies;
    }

    @Override
    public Company getCompanyById(int id) throws Exception {
        final String sql = SQLQuerryConstants.GET_COMPANY_BY_ID;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, id);
        if (rs.next()) {
            final Company company = new Company(rs);
            rs.close();
            return company;
        }
        rs.getStatement().close();
        return null;
    }

    @Override
    public void addCompany(String name, String description) throws Exception {
        final String sql = SQLQuerryConstants.ADD_COMPANY;
        JDBCUtil.getInstance().executeSQL(sql, name, description);
    }

    @Override
    public Discussion getDiscussionById(int id) throws Exception {
        final String sql = SQLQuerryConstants.GET_DISCUSSION_BY_ID;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, id);
        if (rs.next()) {
            final Discussion descussion = new Discussion(rs);
            rs.close();
            return descussion;
        }
        rs.getStatement().close();
        return null;
    }

    @Override
    public List<Discussion> getDiscussionByProblemId(int problemId) throws Exception {
        final List<Discussion> discussions = new ArrayList<Discussion>();
        final String sql = SQLQuerryConstants.GET_DISCUSSION_BY_PROBLEM_ID;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, problemId);
        while (rs.next()) {
            Discussion discussion = new Discussion(rs);
            discussions.add(discussion);
        }
        rs.getStatement().close();
        rs.close();
        return discussions;
    }

    @Override
    public void addDiscussion(int problemID, String name, String subject) throws Exception {
        final String sql = SQLQuerryConstants.ADD_DISCUSSION;
        JDBCUtil.getInstance().executeSQL(sql, problemID, name, subject);
    }

    @Override
    public Problem getProblemById(int problemId) throws Exception {
        final String sql = SQLQuerryConstants.GET_PROBLEM_BY_ID;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, problemId);
        if (rs.next()) {
            final Problem problem = new Problem(rs);
            rs.close();
            return problem;
        }
        rs.getStatement().close();
        return null;
    }

    @Override
    public List<Problem> getProblemsByServiceId(int service_id) throws Exception {
        final List<Problem> problems = new ArrayList<Problem>();
        final String sql = SQLQuerryConstants.GET_PROBLEMS_BY_SERVICE_ID;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, service_id);
        while (rs.next()) {
            Problem problem = new Problem(rs);
            problems.add(problem);
        }
        rs.getStatement().close();
        rs.close();
        return problems;
    }

    @Override
    public void addProblem(int service_id, String name, String description) throws Exception {
        final String sql = SQLQuerryConstants.ADD_PROBLEM;
        JDBCUtil.getInstance().executeSQL(sql, service_id, name, description);
    }

    @Override
    public Service getServiceById(int serviceId) throws Exception {
        final String sql = SQLQuerryConstants.GET_SERVICE_BY_ID;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, serviceId);
        if (rs.next()) {
            final Service service = new Service(rs);
            rs.close();
            return service;
        }
        rs.getStatement().close();
        return null;
    }

    @Override
    public void addService(int companyID, String name, String description) throws Exception {
        final String sql = SQLQuerryConstants.ADD_SERVICE;
        JDBCUtil.getInstance().executeSQL(sql, companyID, name, description);
    }

    @Override
    public List<Company> getCompaniesByName(String name) throws Exception {
        final List<Company> companies = new ArrayList<Company>();
        final String sql = SQLQuerryConstants.GET_COMPANIES_BY_NAME;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, name + "%");
        while (rs.next()) {
            Company company = new Company(rs);
            companies.add(company);
        }
        rs.getStatement().close();
        rs.close();
        return companies;
    }

    @Override
    public List<Service> getServicesByName(String name) throws Exception {
        final List<Service> services = new ArrayList<Service>();
        final String sql = SQLQuerryConstants.GET_SERVICES_BY_NAME;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, name + "%");
        while (rs.next()) {
            Service service = new Service(rs);
            services.add(service);
        }
        rs.getStatement().close();
        rs.close();
        return services;
    }

    @Override
    public List<Problem> getProblemsByName(String name) throws Exception {
        final List<Problem> problems = new ArrayList<Problem>();
        final String sql = SQLQuerryConstants.GET_PROBLEMS_BY_NAME;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, name + "%");
        while (rs.next()) {
            Problem problem = new Problem(rs);
            problems.add(problem);
        }
        rs.getStatement().close();
        rs.close();
        return problems;
    }

    @Override
    public List<Discussion> getDiscussionsBySubject(String subject) throws Exception {
        final List<Discussion> discussions = new ArrayList<Discussion>();
        final String sql = SQLQuerryConstants.GET_DISCUSSION_BY_SUBJECT;
        final ResultSet rs = JDBCUtil.getInstance().executeQuery(sql, subject + "%");
        while (rs.next()) {
            Discussion discussoin = new Discussion(rs);
            discussions.add(discussoin);
        }
        rs.getStatement().close();
        rs.close();
        return discussions;
    }

    @Override
    public Company getCompanyByServiseID(int serviceId) throws Exception {
        return null;
    }

    @Override
    public List<Service> getServiceByCompanyId(int id) {
        return null;
    }
}
