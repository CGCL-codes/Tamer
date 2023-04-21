package com.c2b2.ipoint.scripts;

import com.c2b2.ipoint.model.Channel;
import com.c2b2.ipoint.model.Page;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.Property;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class creates the default channel in a portal
 * <p>
 * iPoint Portal
 * Copyright 2007 C2B2 Consulting Limited. All rights reserved.
 * </p>
 */
public class CreateDefaultChannel implements ScriptBean {

    public CreateDefaultChannel() {
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Channel channel = Channel.getDefaultChannel();
            PrintWriter writer = response.getWriter();
            if (channel == null) {
                Page page = Page.findPage(Property.getPropertyValue("HomePage"));
                channel = Channel.create(Channel.DEFAULT_CHANNEL, page, page.getTemplate());
                List<Page> roots = Page.getRootPages();
                for (Page rootPage : roots) {
                    writer.println("Adding Page " + rootPage.getID() + " " + rootPage.getName() + " to the default channel <br/>");
                    channel.addPage(rootPage);
                }
            } else {
                writer.println("Default Channel already created");
            }
        } catch (Exception e) {
            throw new ServletException("Unable to execute the bean", e);
        }
    }
}
