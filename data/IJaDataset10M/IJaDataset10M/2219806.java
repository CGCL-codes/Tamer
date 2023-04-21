package com.google.gdt.eclipse.designer.gwtext;

import com.google.gdt.eclipse.designer.gwtext.model.widgets.ContainerInfo;

/**
 * Tests for <code>Portal</code>.
 * 
 * @author scheglov_ke
 */
public class PortalTest extends GwtExtModelTest {

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    public void test_parse() throws Exception {
        ContainerInfo panel = parseJavaInfo("public class Test extends Portal {", "  public Test() {", "    PortalColumn firstCol = new PortalColumn();", "    firstCol.setPaddings(10, 10, 0, 10);", "    {", "      Portlet portlet = new Portlet('My portlet', 'Some html');", "      firstCol.add(portlet);;", "    }", "    add(firstCol, new ColumnLayoutData(.33));", "  }", "}");
        panel.refresh();
        assertHierarchy("{this: com.gwtext.client.widgets.portal.Portal} {this} {/add(firstCol, new ColumnLayoutData(.33))/}", "  {implicit-layout: com.gwtext.client.widgets.layout.ColumnLayout} {implicit-layout} {}", "  {new: com.gwtext.client.widgets.portal.PortalColumn} {local-unique: firstCol} {/new PortalColumn()/ /firstCol.setPaddings(10, 10, 0, 10)/ /firstCol.add(portlet)/ /add(firstCol, new ColumnLayoutData(.33))/}", "    {new: com.gwtext.client.widgets.portal.Portlet} {local-unique: portlet} {/new Portlet('My portlet', 'Some html')/ /firstCol.add(portlet)/}", "      {implicit-layout: default} {implicit-layout} {}", "    {new: com.gwtext.client.widgets.layout.ColumnLayoutData} {empty} {/add(firstCol, new ColumnLayoutData(.33))/}");
    }
}
