package com.nyandu.weboffice.common.business;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: gus
 * Date: 17-ene-2005
 * Time: 18:48:07
 * To change this template use File | Settings | File Templates.
 */
public class GuiCommand {

    private int id;

    private String text;

    private String img;

    private String action;

    private String type;

    private String value;

    private Integer renderType;

    private String domId;

    private Integer groupId;

    private String groupImg;

    public String getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getDomId() {
        return domId;
    }

    public void setDomId(String domId) {
        this.domId = domId;
    }

    public Integer getRenderType() {
        return renderType;
    }

    public void setRenderType(Integer renderType) {
        this.renderType = renderType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String tooltip;

    public GuiCommand(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public GuiCommand(int id, Integer renderType, String text, String img, String action, String type, String value, String tooltip, Integer groupId, String groupImg, String domId) {
        this.id = id;
        this.text = text;
        this.img = img;
        this.action = action;
        this.tooltip = tooltip;
        this.renderType = renderType;
        this.domId = domId;
        this.groupId = groupId;
        this.groupImg = groupImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
