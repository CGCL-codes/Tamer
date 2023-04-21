package at.fhjoanneum.aim.sdi.project.tests;

import java.io.IOException;
import at.fhjoanneum.aim.sdi.project.exceptions.DeleteProjectException;
import at.fhjoanneum.aim.sdi.project.exceptions.SVNSetupException;
import at.fhjoanneum.aim.sdi.project.object.Project;
import at.fhjoanneum.aim.sdi.project.service.impl.DeleteProject;
import at.fhjoanneum.aim.sdi.project.service.impl.RepositoryAccessService;
import at.fhjoanneum.aim.sdi.project.service.impl.ServiceImpl;
import at.fhjoanneum.aim.sdi.project.utilities.SVNPropertyLoader;
import at.fhjoanneum.aim.sdi.project.utilities.TemplateGenerator;

public class TestDeleteProjectService {

    public static void main(String[] args) {
        try {
            SVNPropertyLoader loader = new SVNPropertyLoader();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("IN SET_UP");
        try {
            new ServiceImpl("https://10.15.21.240/svn/IMA05", "svnadmin", "svn");
        } catch (SVNSetupException e) {
            System.out.println("Kann nicht auf REPO zugreifen!");
        }
        DeleteProject dpr = new DeleteProject();
        Project p = new Project();
        p.setStructured(true);
        p.setTargetURL("/newTestHome6");
        TemplateGenerator tg = new TemplateGenerator();
        tg.generateTemplate("E:\\PROJEKTE_PROG\\2008\\AdminSVN\\src\\main\\resources\\access.txt");
        tg.parseTemplate("");
        RepositoryAccessService ras = new RepositoryAccessService(tg.getRepoList(), tg.getGroupList());
        try {
            dpr.deleteProject(p);
        } catch (DeleteProjectException e) {
            System.out.println("KANN PROJEKT NICHT L�SCHEN!");
            e.printStackTrace();
        }
        System.out.println("\nDONE!");
    }
}
