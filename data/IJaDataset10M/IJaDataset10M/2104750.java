package com.openospc.velocity.management;

import javax.servlet.http.HttpSession;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import com.openospc.velocity.HTMLVelocity;

public class ProcessWizard extends HTMLVelocity {

    public void doTemplate() {
        System.out.println("1");
        try {
            System.out.println("2");
            HttpSession session = request.getSession();
            if (request.getParameter("wizardon") != null && (request.getParameter("wizardon")).equals("wizardon")) {
                System.out.println("3");
                session.setAttribute("wizardon", (String) request.getParameter("wizardon"));
                System.out.println("4");
                template = Velocity.getTemplate("/com/openospc/velocity/management/processwizard/wizard.vm");
                System.out.println("5");
            } else if (session.getAttribute("wizardon") != null && ((String) session.getAttribute("wizardon")).equals("1")) {
                System.out.println("6");
                template = Velocity.getTemplate("/com/openospc/velocity/management/processwizard/wizard.vm");
                System.out.println("7");
            }
            System.out.println("10");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
