package br.com.dimension.imap2rest.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import br.com.dimension.imap2rest.holders.ClientSession;
import br.com.dimension.imap2rest.holders.SimpleMessage;

public class Mailbox extends Resource {

    String ticket;

    ClientSession cs;

    String mailbox;

    public Mailbox(Context context, Request request, Response response) {
        super(context, request, response);
        cs = (ClientSession) this.getRequest().getAttributes().get("client");
        mailbox = (String) this.getRequest().getAttributes().get("folder");
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
    }

    private void listFolder(List<String> m, Folder folder) {
        Folder flist[] = null;
        try {
            flist = folder.list();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        if (flist.length > 1) {
            for (Folder f : flist) {
                listFolder(m, f);
            }
        } else {
            m.add(folder.getFullName().replace("/", "*"));
            System.out.println("Folder " + folder.getFullName().replace("/", "*"));
        }
    }

    private Representation getInfo(Variant var) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Folder fldr = cs.getStore().getFolder(mailbox);
            fldr.open(Folder.READ_ONLY);
            int cc;
            cc = fldr.getMessageCount();
            map.put("messagecount", Integer.toString(cc));
            map.put("foldername", fldr.getName());
            cc = fldr.getNewMessageCount();
            map.put("newmessagecount", Integer.toString(cc));
            fldr.close(false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new JsonRepresentation(new JSONObject(map));
    }

    private Representation getList(Variant var) {
        List<String> m = new ArrayList<String>();
        JSONArray j = null;
        try {
            listFolder(m, cs.getStore().getDefaultFolder());
            j = new JSONArray(m);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new JsonRepresentation(j);
    }

    private Representation getMailbox(Variant var) {
        JSONArray j;
        j = new JSONArray();
        Representation result = null;
        List<JSONObject> list = new ArrayList<JSONObject>();
        int ini, end;
        ini = end = 1;
        if (this.getRequest().getAttributes().get("ini") != null) {
            ini = Integer.parseInt((String) this.getRequest().getAttributes().get("ini"));
        }
        if (this.getRequest().getAttributes().get("end") != null) {
            end = Integer.parseInt((String) this.getRequest().getAttributes().get("end"));
        }
        try {
            Folder fldr = cs.getStore().getFolder(mailbox);
            fldr.open(Folder.READ_ONLY);
            int count = fldr.getMessageCount();
            if (end == 1) end = count;
            Message messages[] = fldr.getMessages(ini, end);
            for (Message m : messages) {
                SimpleMessage sm = new SimpleMessage();
                sm.setSubject(m.getSubject());
                sm.setMsgid(m.getMessageNumber());
                sm.setDate(m.getSentDate());
                for (Address a : m.getFrom()) {
                    if (a.getType().equals("rfc822")) {
                    }
                    sm.addFrom(a.toString(), a.getType());
                }
                list.add(sm.getJsonObject());
            }
            fldr.close(false);
        } catch (MessagingException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonRepresentation(new JSONArray(list));
    }

    public Representation getRepresentation(Variant var) {
        if (mailbox == null) return getList(var);
        if (mailbox != null) {
            mailbox = mailbox.replace("*", "/");
            try {
                mailbox = URLDecoder.decode(mailbox, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(getRequest().getResourceRef().getLastSegment().equals("info"));
            if (getRequest().getResourceRef().getLastSegment().equals("info")) return getInfo(var);
            return getMailbox(var);
        }
        return null;
    }
}
