package jwinforms;

public class LinkLabel extends Label {

    public EventHandler<Object, LinkLabelLinkClickedEventArgs> LinkClicked;

    public LinkArea LinkArea;

    public LinkHolder Links = new LinkHolder();
}
