package com.shengyijie.model.json;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.shengyijie.context.ContextApplication;
import com.shengyijie.model.object.Listobject.AdvertPicList;
import com.shengyijie.model.object.Listobject.GuesstBookList;
import com.shengyijie.model.object.Listobject.ProPictureList;
import com.shengyijie.model.object.Listobject.ProjectList;
import com.shengyijie.model.object.Listobject.PushMessageList;
import com.shengyijie.model.object.baseobject.AdvertPic;
import com.shengyijie.model.object.baseobject.GuesstBook;
import com.shengyijie.model.object.baseobject.ProPicture;
import com.shengyijie.model.object.baseobject.Project;
import com.shengyijie.model.object.baseobject.PushMessage;
import com.shengyijie.model.object.baseobject.RenrenData;
import com.shengyijie.model.object.baseobject.User;

/** @title: JsonParser DRIVER 1.0 
 * @description: JsonParser DRIVER 1.0 
 * @copyright: Diipo (c) 2011.06 
 * @company: Diipo 
 * @author liukun 
 * @version 1.0 
 */
public class JsonParser {

    private static JsonParser jsonParser;

    public JsonParser() {
    }

    public static JsonParser getInstance() {
        if (jsonParser == null) {
            jsonParser = new JsonParser();
        }
        return jsonParser;
    }

    public static JSONObject getJSON(HttpResponse resp) {
        try {
            HttpEntity entity = resp.getEntity();
            InputStreamReader isr = new InputStreamReader(entity.getContent());
            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String result;
            result = br.readLine();
            while (result != null) {
                sb.append(result);
                result = br.readLine();
            }
            String temp = sb.toString();
            Log.e(ContextApplication.TAG, "下行: " + temp);
            JSONObject json;
            if (temp.startsWith("[")) {
                JSONArray jsona = new JSONArray(temp);
                json = new JSONObject();
                json.put("array", jsona);
            } else {
                json = new JSONObject(temp);
            }
            isr.close();
            br.close();
            sb = null;
            entity = null;
            return json;
        } catch (Exception e) {
            return null;
        }
    }

    public RenrenData getRenrenData(HttpResponse resp) {
        try {
            RenrenData renrenData = new RenrenData();
            JSONObject json = getJSON(resp);
            renrenData.setSession_key(json.getString("access_token"));
            return renrenData;
        } catch (JSONException e) {
            return null;
        }
    }

    public String getSinaData(HttpResponse resp) {
        try {
            JSONObject json = getJSON(resp);
            if (json.has("access_token")) {
                if (json.getInt("access_token") == 0) {
                    return json.getString("access_token");
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public User getRegister(HttpResponse resp) {
        User user = new User();
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                user.setState(json.getInt("error_code"));
            } else {
                user.setUserID(json.getString("USER_ID"));
                user.setUserType(json.getInt("USER_TYPE"));
                user.setSession_ID(json.getString("SESSION_ID"));
                user.setState(2000);
            }
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public int getResultCode(HttpResponse resp) {
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return json.getInt("error_code");
            }
        } catch (Exception e) {
        }
        return 0;
    }

    public User getLoginUser(HttpResponse resp) {
        User user = new User();
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                user.setState(json.getInt("error_code"));
            } else {
                user.setUserID(json.getString("id"));
                user.setEmail(json.getString("email"));
                user.setUser_name(json.getString("user_name"));
                user.setState(2000);
            }
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public PushMessageList getPushMessage(HttpResponse resp) {
        PushMessageList messageList = new PushMessageList();
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray("array");
                PushMessage message;
                JSONObject temp;
                for (int i = 0; i < jsonArray.length(); i++) {
                    temp = jsonArray.getJSONObject(i);
                    message = new PushMessage();
                    message.setID(temp.getString("id"));
                    message.setTitle(temp.getString("title"));
                    message.setImageUrl(temp.getString("newsimg"));
                    message.setDes(temp.getString("content"));
                    message.setDetail(temp.getString("contents"));
                    message.setType(temp.getInt("typeid"));
                    message.setNews_statu(temp.getString("news_statu"));
                    message.setCreatetime(temp.getString("createdate"));
                    message.setStatus(temp.getInt("status"));
                    message.setPro_url(temp.getString("projecturl"));
                    message.setError_code(2000);
                    messageList.addItem(message);
                }
            }
            return messageList;
        } catch (Exception e) {
            return null;
        }
    }

    public PushMessage getMessageDetail(HttpResponse resp) {
        try {
            PushMessage message = new PushMessage();
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                message.setID(json.getString("id"));
                message.setTitle(json.getString("title"));
                message.setImageUrl(json.getString("newsimg"));
                message.setDes(json.getString("content"));
                message.setDetail(json.getString("contents"));
                message.setType(json.getInt("typeid"));
                message.setNews_statu(json.getString("news_statu"));
                message.setCreatetime(json.getString("createdate"));
                message.setStatus(json.getInt("status"));
                message.setError_code(2000);
            }
            return message;
        } catch (Exception e) {
            return null;
        }
    }

    public int[] getAttentionProgectID(HttpResponse resp) {
        int[] projectList;
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray("array");
                projectList = new int[jsonArray.length()];
                JSONObject temp;
                for (int i = 0; i < jsonArray.length(); i++) {
                    temp = jsonArray.getJSONObject(i);
                    projectList[i] = temp.getInt("pro_id");
                }
            }
            return projectList;
        } catch (Exception e) {
            return null;
        }
    }

    public ProjectList getAttentionProgect(HttpResponse resp) {
        ProjectList projectList = new ProjectList();
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray("array");
                Project project;
                JSONObject temp;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        temp = jsonArray.getJSONObject(i);
                        project = new Project();
                        project.setID(temp.getInt("id"));
                        project.setPro_id(temp.getString("pro_id"));
                        project.setPro_name(temp.getString("pro_name"));
                        project.setPp_logo(temp.getString("pp_logo"));
                        project.setPro_define(temp.getString("pro_define"));
                        project.setPro_industry(temp.getInt("pro_industry"));
                        project.setPro_about(temp.getString("pro_about"));
                        project.setPro_fenxi(temp.getString("pro_fenxi"));
                        project.setPro_com_name(temp.getString("pro_com"));
                        project.setUser_id(temp.getString("user_id"));
                        project.setPro_pic_show(temp.getString("pro_pic_show"));
                        project.setPro_status(temp.getInt("pro_status"));
                        project.setCreate_date(temp.getString("create_date"));
                        project.setAccount_type(temp.getInt("account_type"));
                        project.setInvested(temp.getInt("invested"));
                        project.setStritem(temp.getInt("stritem"));
                        project.setPostn_id(temp.getString("postn_id"));
                        project.setIsupdate(temp.getInt("isupdate"));
                        project.setZs_phone(temp.getString("zs_phone"));
                        project.setZs_city(temp.getInt("zs_city"));
                        project.setIsrecommend(temp.getInt("isrecommend"));
                        projectList.addItem(project);
                    } catch (Exception e) {
                    }
                }
            }
            return projectList;
        } catch (Exception e) {
            return null;
        }
    }

    public ProjectList getSearchProgect(HttpResponse resp) {
        ProjectList projectList = new ProjectList();
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                projectList.setIsTuijian(json.getInt("tuijian"));
                Project project;
                JSONObject temp;
                for (int i = 0; i < 50; i++) {
                    try {
                        if (json.has(i + "")) {
                            temp = json.getJSONObject(i + "");
                            project = new Project();
                            project.setID(temp.getInt("id"));
                            project.setPro_id(temp.getString("pro_id"));
                            project.setPro_name(temp.getString("pro_name"));
                            project.setPp_logo(temp.getString("pp_logo"));
                            project.setPro_define(temp.getString("pro_define"));
                            project.setPro_industry(temp.getInt("pro_industry"));
                            project.setPro_about(temp.getString("pro_about"));
                            project.setPro_fenxi(temp.getString("pro_fenxi"));
                            project.setPro_com_name(temp.getString("pro_com"));
                            project.setUser_id(temp.getString("user_id"));
                            project.setPro_pic_show(temp.getString("pro_pic_show"));
                            project.setPro_status(temp.getInt("pro_status"));
                            project.setCreate_date(temp.getString("create_date"));
                            project.setAccount_type(temp.getInt("account_type"));
                            project.setInvested(temp.getInt("invested"));
                            project.setStritem(temp.getInt("stritem"));
                            project.setPostn_id(temp.getString("postn_id"));
                            project.setIsupdate(temp.getInt("isupdate"));
                            project.setZs_phone(temp.getString("zs_phone"));
                            project.setZs_city(temp.getInt("zs_city"));
                            project.setIsrecommend(temp.getInt("isrecommend"));
                            projectList.addItem(project);
                        }
                    } catch (Exception e) {
                    }
                }
            }
            return projectList;
        } catch (Exception e) {
            return null;
        }
    }

    public Project getProgectDetail(HttpResponse resp) {
        Project project = new Project();
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                project.setID(json.getInt("id"));
                project.setPro_id(json.getString("pro_id"));
                project.setPro_name(json.getString("pro_name"));
                project.setPp_logo(json.getString("pp_logo"));
                project.setPro_define(json.getString("pro_define"));
                project.setPro_industry(json.getInt("pro_industry"));
                project.setPro_about(json.getString("pro_about"));
                project.setPro_fenxi(json.getString("pro_fenxi"));
                project.setPro_com_name(json.getString("pro_com"));
                project.setUser_id(json.getString("user_id"));
                project.setPro_pic_show(json.getString("pro_pic_show"));
                project.setPro_status(json.getInt("pro_status"));
                project.setCreate_date(json.getString("create_date"));
                project.setAccount_type(json.getInt("account_type"));
                project.setInvested(json.getInt("invested"));
                project.setStritem(json.getInt("stritem"));
                project.setPostn_id(json.getString("postn_id"));
                project.setIsupdate(json.getInt("isupdate"));
                project.setZs_phone(json.getString("zs_phone"));
                project.setZs_city(json.getInt("zs_city"));
                project.setPro_url(json.getString("pro_url"));
                project.setIsrecommend(json.getInt("isrecommend"));
            }
            return project;
        } catch (Exception e) {
            return null;
        }
    }

    public ProPictureList getProPicList(HttpResponse resp) {
        ProPictureList proPictureList = new ProPictureList();
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray("array");
                ProPicture proPicture;
                JSONObject temp;
                for (int i = 0; i < jsonArray.length(); i++) {
                    temp = jsonArray.getJSONObject(i);
                    proPicture = new ProPicture();
                    proPicture.setId(temp.getInt("id"));
                    proPicture.setPro_id(temp.getString("pro_id"));
                    proPicture.setPro_pic(temp.getString("proPic"));
                    proPicture.setCreatetime(temp.getString("createdate"));
                    proPicture.setDescribe(temp.getString("describe"));
                    proPictureList.addItem(proPicture);
                }
            }
            return proPictureList;
        } catch (Exception e) {
            return null;
        }
    }

    public String getAbout(HttpResponse resp, int type) {
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else if (json.has(type + "")) {
                return json.getString(type + "");
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String getAdvers(HttpResponse resp, int type) {
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else if (json.has(type + "")) {
                return json.getString(type + "");
            }
        } catch (Exception e) {
        }
        return null;
    }

    public AdvertPicList getAdert(HttpResponse resp) {
        AdvertPicList advertPicList = new AdvertPicList();
        AdvertPic advertPic;
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray("array");
                JSONObject temp;
                for (int i = 0; i < jsonArray.length(); i++) {
                    temp = jsonArray.getJSONObject(i);
                    advertPic = new AdvertPic();
                    advertPic.setId(temp.getInt("id"));
                    advertPic.setPro_id(temp.getString("pro_id"));
                    advertPic.setPro_name(temp.getString("pro_name"));
                    advertPic.setPic_url(temp.getString("pic_url"));
                    advertPic.setCreate(temp.getString("create"));
                    advertPicList.addItem(advertPic);
                }
            }
            return advertPicList;
        } catch (Exception e) {
            return null;
        }
    }

    public AdvertPicList getAderts(HttpResponse resp) {
        AdvertPicList advertPicList = new AdvertPicList();
        AdvertPic advertPic;
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray("array");
                JSONObject temp;
                for (int i = 0; i < jsonArray.length(); i++) {
                    temp = jsonArray.getJSONObject(i);
                    advertPic = new AdvertPic();
                    advertPic.setPro_id(temp.getString("pro_id"));
                    advertPic.setPic_url(temp.getString("proPic"));
                    advertPic.setCreate(temp.getString("createdate"));
                    advertPicList.addItem(advertPic);
                }
            }
            return advertPicList;
        } catch (Exception e) {
            return null;
        }
    }

    public GuesstBookList getGuesstBookList(HttpResponse resp) {
        GuesstBookList guesstBookList = new GuesstBookList();
        GuesstBook guesstBook;
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray("array");
                JSONObject temp;
                for (int i = 0; i < jsonArray.length(); i++) {
                    temp = jsonArray.getJSONObject(i);
                    guesstBook = new GuesstBook();
                    guesstBook.setId(temp.getString("id"));
                    guesstBook.setName(temp.getString("guestname"));
                    guesstBook.setMobile(temp.getString("mobile"));
                    guesstBook.setEmail(temp.getString("email"));
                    guesstBook.setAddress(temp.getString("address"));
                    guesstBook.setContent(temp.getString("content"));
                    guesstBook.setStatus(temp.getInt("status"));
                    guesstBook.setTime(temp.getString("time"));
                    guesstBookList.addItem(guesstBook);
                }
            }
            return guesstBookList;
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getNewsType(HttpResponse resp) {
        String[] list = new String[50];
        try {
            JSONObject json = getJSON(resp);
            if (json.has("error_code")) {
                return null;
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray("array");
                JSONObject temp;
                for (int i = 0; i < jsonArray.length(); i++) {
                    temp = jsonArray.getJSONObject(i);
                    int id = temp.getInt("id");
                    list[id] = temp.getString("type");
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }
}
