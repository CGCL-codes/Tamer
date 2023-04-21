package org.dengues.model;

import java.util.Map;
import org.dengues.model.component.ComponentFactory;
import org.dengues.model.component.ComponentPackage;
import org.dengues.model.component.impl.ComponentPackageImpl;
import org.dengues.model.database.DatabaseFactory;
import org.dengues.model.database.DatabasePackage;
import org.dengues.model.database.impl.DatabasePackageImpl;
import org.dengues.model.project.ProjectFactory;
import org.dengues.model.project.ProjectPackage;
import org.dengues.model.project.impl.ProjectPackageImpl;
import org.dengues.model.reports.ReportsFactory;
import org.dengues.model.reports.ReportsPackage;
import org.dengues.model.reports.impl.ReportsPackageImpl;
import org.dengues.model.warehouse.WarehouseFactory;
import org.dengues.model.warehouse.WarehousePackage;
import org.dengues.model.warehouse.impl.WarehousePackageImpl;
import org.eclipse.emf.ecore.EPackage;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-1-24 qiang.zhang $
 * 
 */
public class DenguesModelManager {

    private static DatabaseFactory databaseFactory = getDatabaseFactory();

    public static DatabaseFactory getDatabaseFactory() {
        if (databaseFactory == null) {
            DatabasePackageImpl.init();
            Map registry = EPackage.Registry.INSTANCE;
            String URI = DatabasePackageImpl.eNS_URI;
            DatabasePackage databasePackage = (DatabasePackage) registry.get(URI);
            databaseFactory = databasePackage.getDatabaseFactory();
        }
        return databaseFactory;
    }

    private static ComponentFactory componentFactory = getComponentFactory();

    public static ComponentFactory getComponentFactory() {
        if (componentFactory == null) {
            ComponentPackageImpl.init();
            Map registry = EPackage.Registry.INSTANCE;
            String URI = ComponentPackageImpl.eNS_URI;
            ComponentPackage databasePackage = (ComponentPackage) registry.get(URI);
            componentFactory = databasePackage.getComponentFactory();
        }
        return componentFactory;
    }

    private static WarehouseFactory warehouseFactory = getWarehouseFactory();

    public static WarehouseFactory getWarehouseFactory() {
        if (warehouseFactory == null) {
            WarehousePackageImpl.init();
            Map registry = EPackage.Registry.INSTANCE;
            String URI = WarehousePackageImpl.eNS_URI;
            WarehousePackage databasePackage = (WarehousePackage) registry.get(URI);
            warehouseFactory = databasePackage.getWarehouseFactory();
        }
        return warehouseFactory;
    }

    private static ProjectFactory projectFactory = getProjectFactory();

    public static ProjectFactory getProjectFactory() {
        if (projectFactory == null) {
            ProjectPackageImpl.init();
            Map registry = EPackage.Registry.INSTANCE;
            String URI = ProjectPackageImpl.eNS_URI;
            ProjectPackage projectPackage = (ProjectPackage) registry.get(URI);
            projectFactory = projectPackage.getProjectFactory();
        }
        return projectFactory;
    }

    private static ReportsFactory reportsFactory = getReportsFactory();

    public static ReportsFactory getReportsFactory() {
        if (reportsFactory == null) {
            ReportsPackageImpl.init();
            Map registry = EPackage.Registry.INSTANCE;
            String URI = ReportsPackageImpl.eNS_URI;
            ReportsPackage databasePackage = (ReportsPackage) registry.get(URI);
            reportsFactory = databasePackage.getReportsFactory();
        }
        return reportsFactory;
    }

    public static void registerAllEmfPackages() {
        getDatabaseFactory();
        getComponentFactory();
        getWarehouseFactory();
        getProjectFactory();
        getReportsFactory();
    }
}
