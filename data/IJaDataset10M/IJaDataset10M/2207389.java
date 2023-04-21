package org.esb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.esb.hive.DatabaseService;
import org.esb.jmx.JHiveRegistryException;
import org.esb.model.Filter;
import org.esb.model.FilterAttribute;
import org.esb.model.FilterConfiguration;
import org.esb.model.MediaFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectDao {

    private static Logger _log = LoggerFactory.getLogger(ProjectDao.class);

    private ProjectDao() {
    }

    public static int setProject(final org.esb.model.Project p) throws JHiveRegistryException {
        _log.debug("ProjectId=" + p.getId());
        int projectId = p.getId();
        try {
            Connection con = DatabaseService.getConnection();
            boolean update = p.getId() > 0;
            PreparedStatement stmt = null;
            if (!update) {
                stmt = con.prepareStatement("insert into project (name, created) values (?,?)", Statement.RETURN_GENERATED_KEYS);
            } else {
                stmt = con.prepareStatement("update project set name=?, created=? where id=?");
                stmt.setInt(3, p.getId());
            }
            stmt.setString(1, p.getName());
            stmt.setTimestamp(2, new Timestamp(p.getDate().getTime()));
            stmt.execute();
            if (!update) {
                ResultSet rsj = stmt.getGeneratedKeys();
                if (!rsj.next()) throw new JHiveRegistryException("could not fetch GeneratedKeys from Database");
                projectId = rsj.getInt(1);
            }
            p.setId(projectId);
            stmt = con.prepareStatement("insert into project_files (file_id, project_id) values (?,?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stmt_delete = con.prepareStatement("delete from project_files where file_id=? and project_id=?");
            PreparedStatement stmt_select_all = con.prepareStatement("select * from project_files where project_id=?");
            stmt_select_all.setInt(1, projectId);
            ResultSet rs = stmt_select_all.executeQuery();
            Map<String, String> db_files = new HashMap<String, String>();
            while (rs.next()) {
                db_files.put(rs.getString("file_id"), rs.getString("id"));
            }
            List<org.esb.model.MediaFile> files = p.getMediaFiles();
            for (org.esb.model.MediaFile file : files) {
                int pfileid = Integer.parseInt((String) file.get("id"));
                if (!db_files.containsKey(file.get("id"))) {
                    stmt.setString(1, (String) file.get("id"));
                    stmt.setInt(2, projectId);
                    stmt.execute();
                    ResultSet rsj = stmt.getGeneratedKeys();
                    if (!rsj.next()) throw new JHiveRegistryException("could not fetch GeneratedKeys from Database");
                    pfileid = rsj.getInt(1);
                } else {
                    pfileid = Integer.parseInt(db_files.get(file.get("id")));
                    db_files.remove(file.get("id"));
                }
                List<Filter> filters = file.getFilters();
                for (Filter filter : filters) {
                    Statement st = con.createStatement();
                    ResultSet rs1 = st.executeQuery("select * from project_filter where project_files_id=" + pfileid + " and filter_name='" + filter.getFilterName() + "'");
                    int filterid = 0;
                    if (!rs1.next()) {
                        Statement stmt2 = con.createStatement();
                        stmt2.execute("insert into project_filter (filter_name, project_files_id) values('" + filter.getFilterName() + "'," + pfileid + ")", Statement.RETURN_GENERATED_KEYS);
                        ResultSet rsj2 = stmt2.getGeneratedKeys();
                        if (!rsj2.next()) throw new JHiveRegistryException("could not fetch GeneratedKeys from Database");
                        filterid = rsj2.getInt(1);
                    } else {
                        filterid = rs1.getInt("id");
                    }
                    con.createStatement().execute("delete from project_filter_params where filter_id=" + filterid);
                    for (String key : filter.keySet()) {
                        con.createStatement().execute("insert into project_filter_params (filter_id,param_key,param_val) values (" + filterid + ",'" + key + "','" + filter.get(key) + "') ");
                    }
                }
            }
            for (String id : db_files.keySet()) {
                stmt_delete.setString(1, id);
                stmt_delete.setInt(2, projectId);
                stmt_delete.execute();
            }
            Map<Integer, Integer> dbprofiles = new HashMap<Integer, Integer>();
            {
                Statement st = con.createStatement();
                ResultSet rs1 = st.executeQuery("select * from project_profiles where project_id=" + p.getId());
                while (rs1.next()) {
                    dbprofiles.put(rs1.getInt("id"), rs1.getInt("id"));
                }
            }
            List<org.esb.model.Profile> profiles = p.getProfiles();
            for (org.esb.model.Profile profile : profiles) {
                _log.debug(profile.toString());
                Statement st = con.createStatement();
                ResultSet rs1 = st.executeQuery("select * from project_profiles where project_id=" + p.getId() + " and profile_name='" + profile.get("profile_name") + "'");
                int profileid = 0;
                if (!rs1.next()) {
                    Statement stmt2 = con.createStatement();
                    stmt2.execute("insert into project_profiles (profile_name, project_id) values('" + profile.get("profile_name") + "'," + p.getId() + ")", Statement.RETURN_GENERATED_KEYS);
                    ResultSet rsj2 = stmt2.getGeneratedKeys();
                    if (!rsj2.next()) throw new JHiveRegistryException("could not fetch GeneratedKeys from Database");
                    profileid = rsj2.getInt(1);
                } else {
                    dbprofiles.remove(rs1.getInt("id"));
                    profileid = rs1.getInt("id");
                }
                con.createStatement().execute("delete from project_profiles_param where project_profile_id=" + profileid);
                for (String key : profile.keySet()) {
                    con.createStatement().execute("insert into project_profiles_param (project_profile_id,param_key,param_val) values (" + profileid + ",'" + key + "','" + profile.get(key) + "') ");
                }
            }
            for (Integer project_profile_id : dbprofiles.keySet()) {
                con.createStatement().execute("delete from project_profiles where id=" + project_profile_id);
                con.createStatement().execute("delete from project_profiles_param where project_profile_id=" + project_profile_id);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new JHiveRegistryException(ex.getMessage());
        }
        return projectId;
    }

    public static org.esb.model.Project getProject(final int id) throws JHiveRegistryException {
        org.esb.model.Project result = new org.esb.model.Project();
        try {
            Connection con = DatabaseService.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from project where id=" + id);
            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setDate(rs.getTimestamp("created"));
                result.setName(rs.getString("name"));
                Statement stmt_files = con.createStatement();
                ResultSet rs_files = stmt_files.executeQuery("select * from project_files where project_id=" + id);
                while (rs_files.next()) {
                    MediaFile file = MediaFilesDao.getMediaFile(rs_files.getInt("file_id"));
                    result.addMediaFile(file);
                    Statement stmt_filter = con.createStatement();
                    ResultSet rs_filter = stmt_filter.executeQuery("select * from project_filter where project_files_id=" + rs_files.getInt("id"));
                    while (rs_filter.next()) {
                        FilterAttribute attr = new FilterAttribute();
                        attr.setName(rs_filter.getString("filter_name"));
                        Filter filter = new Filter();
                        file.addFilter(filter);
                        filter.setFilterName(rs_filter.getString("filter_name"));
                        Statement stmt_filter_param = con.createStatement();
                        ResultSet rs_filter_param = stmt_filter_param.executeQuery("select * from project_filter_params where filter_id=" + rs_filter.getInt("id"));
                        while (rs_filter_param.next()) {
                            attr.put(rs_filter_param.getString("param_key"), rs_filter_param.getString("param_val"));
                            filter.setAttribute(rs_filter_param.getString("param_key"), rs_filter_param.getString("param_val"));
                        }
                    }
                }
                Statement stmt_profiles = con.createStatement();
                ResultSet rs_profiles = stmt_profiles.executeQuery("select * from project_profiles where project_id=" + id);
                while (rs_profiles.next()) {
                    long profile_id = rs_profiles.getLong("id");
                    org.esb.model.Profile profile = new org.esb.model.Profile();
                    result.addProfile(profile);
                    Statement stmt_profile_param = con.createStatement();
                    ResultSet rs_profileparam = stmt_profile_param.executeQuery("select * from project_profiles_param where project_profile_id=" + profile_id);
                    while (rs_profileparam.next()) {
                        profile.setAttribute(rs_profileparam.getString("param_key"), rs_profileparam.getString("param_val"));
                    }
                }
            }
        } catch (Exception ex) {
            _log.error("could not fetch Project from Database!", ex.getMessage());
            throw new JHiveRegistryException(ex.getMessage());
        }
        _log.debug("Project resolved:" + result);
        return result;
    }

    public static List<org.esb.model.Project> getProjectList() throws JHiveRegistryException {
        _log.debug("List<org.esb.model.Project> getProjectList() throws JHiveRegistryException");
        List<org.esb.model.Project> result = new ArrayList<org.esb.model.Project>();
        try {
            Connection con = DatabaseService.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from project");
            while (rs.next()) {
                org.esb.model.Project project = new org.esb.model.Project();
                project.setDate(rs.getTimestamp("created"));
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                result.add(project);
            }
        } catch (Exception ex) {
            _log.error("Error fetching projects", ex);
            throw new JHiveRegistryException(ex.getMessage());
        }
        _log.debug("result:size:" + result.size());
        return result;
    }
}
