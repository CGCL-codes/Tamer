package org.blueoxygen.test.cimande;

import org.blueoxygen.cimande.rolemapper.*;
import org.blueoxygen.cimande.descriptor.*;
import java.sql.*;
import java.util.*;

public class RoleSiteMapperViewTest {

    public static void main(String[] args) {
        String sRoleId = "";
        String sSiteId = "";
        RoleSiteMapper rsMapper;
        RoleSitePrivilageMapper rspMapper;
        Map mRSPMapper, mModuleFunction;
        Iterator x, y, z;
        ModuleFunction moduleFunction;
        sRoleId = "3";
        sSiteId = "EDDCA9DCE41A14EE5D5ABE7E3C";
        String rsValue, rspValue;
        RoleSite roleSite;
        Site site;
        try {
            rsMapper = new RoleSiteMapper(sRoleId);
            WorkflowRole role = new WorkflowRole(sRoleId);
            System.out.println("Role Id:" + role.getId());
            System.out.println("Role Name:" + role.getName());
            System.out.println("Role Description:" + role.getDescription());
            System.out.println("--------------------");
            mRSPMapper = rsMapper.getRoleSitePrivilageMap();
            x = mRSPMapper.keySet().iterator();
            while (x.hasNext()) {
                rsValue = (String) x.next();
                System.out.println("Role Site ID:" + rsValue);
                rspMapper = (RoleSitePrivilageMapper) mRSPMapper.get(rsValue);
                roleSite = (RoleSite) rspMapper.getRoleSite();
                site = roleSite.getSite();
                System.out.println("Site Name: " + site.getName());
                mModuleFunction = rspMapper.getModuleFunctionMap();
                y = mModuleFunction.keySet().iterator();
                while (y.hasNext()) {
                    rspValue = (String) y.next();
                    moduleFunction = (ModuleFunction) mModuleFunction.get(rspValue);
                    System.out.print("  " + moduleFunction.getDescription());
                    System.out.println("(" + rspValue + ")");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e);
        } catch (Exception e) {
            System.err.println("RoleSiteMapperTest Error: " + e);
        }
    }
}
