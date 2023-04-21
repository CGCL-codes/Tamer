package com.handjoys.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.handjoys.dbpool.DBProxy;
import com.handjoys.pojo.Player;
import com.handjoys.pojo.SpriteHome;

/**
 * 用户及相关信息
 * @author 汉娱网络.技术部.lifw
 * 
 */
public class PlayUser extends Player {

    /** 当前用户的所有宠物 */
    public PlaySprite[] sprites = new PlaySprite[0];

    public void load(Long playerid) {
        List<HashMap> result = DBProxy.QeurySelect("select * from Player where playerid=" + playerid);
        if (result != null && result.size() > 0) {
            this.setPlayerid(playerid);
            HashMap item = result.get(0);
            this.setPlayerid((Long) item.get("playerid"));
            this.setActivetime((Date) item.get("activetime"));
            this.setAddress((String) item.get("address"));
            this.setBirthday((String) item.get("birthday"));
            this.setCharacternum((String) item.get("characternum"));
            this.setCharactertype((String) item.get("charactertype"));
            this.setCity((String) item.get("city"));
            this.setCountry((String) item.get("country"));
            this.setCreatetime((Date) item.get("createtime"));
            this.setDegree((Integer) item.get("degree"));
            this.setEmail((String) item.get("email"));
            this.setFatherpassword((String) item.get("fatherpassword"));
            this.setFullname((String) item.get("fullname"));
            this.setMobiletele((String) item.get("mobiletele"));
            this.setPassword((String) item.get("password"));
            this.setPlayerid((Long) item.get("playerid"));
            this.setPostcode((String) item.get("postcode"));
            this.setProvince((String) item.get("province"));
            this.setRecommend((String) item.get("recommend"));
            this.setSex((Integer) item.get("sex"));
            this.setState((Integer) item.get("state"));
            this.setTelephone1((String) item.get("telephone1"));
            this.setTelephone2((String) item.get("telephone2"));
            this.setTelephone3((String) item.get("telephone3"));
            this.setType((Integer) item.get("type"));
            this.setUsername((String) item.get("username"));
            this.setValidateconfig((String) item.get("validateconfig"));
            String sql = "select s.*, c.spritecatename, c.spritecatetype from Sprite s left join SpriteCate c on s.spritecateid=c.spritecateid";
            sql += " where c.spritecateid is not null and s.spritestatus=1 and s.playerid=" + playerid + "";
            result = DBProxy.QeurySelect(sql);
            if (result != null && result.size() > 0) {
                this.sprites = new PlaySprite[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    item = result.get(i);
                    this.sprites[i] = new PlaySprite();
                    this.sprites[i].spriteCate.setSpritecateid((Long) item.get("spritecateid"));
                    this.sprites[i].spriteCate.setSpritecatename((String) item.get("spritecatename"));
                    this.sprites[i].spriteCate.setSpritecatetype((Integer) item.get("spritecatetype"));
                    this.sprites[i].setExpvalue((Integer) item.get("expvalue"));
                    this.sprites[i].setPetintellect((Integer) item.get("petintellect"));
                    this.sprites[i].setPlayerid((Long) item.get("playerid"));
                    this.sprites[i].setPreventchat((Integer) item.get("preventchat"));
                    this.sprites[i].setSpriteage((Integer) item.get("spriteage"));
                    this.sprites[i].setSpriteaggressivity((Integer) item.get("spriteaggressivity"));
                    this.sprites[i].setSpritecateid((Long) item.get("spritecateid"));
                    this.sprites[i].setSpritecharm((Integer) item.get("spritecharm"));
                    this.sprites[i].setSpritecolor((Integer) item.get("spritecolor"));
                    this.sprites[i].setSpritecreatetime((Date) item.get("spritecreatetime"));
                    this.sprites[i].setSpritecredit((Integer) item.get("spritecredit"));
                    this.sprites[i].setSpritedefend((Integer) item.get("spritedefend"));
                    this.sprites[i].setSpritedegree((Integer) item.get("spritedegree"));
                    this.sprites[i].setSpritefollowid((Integer) item.get("spritefollowid"));
                    this.sprites[i].setSpritehealth((Integer) item.get("spritehealth"));
                    this.sprites[i].setSpritehunger((Integer) item.get("spritehunger"));
                    this.sprites[i].setSpriteid((Long) item.get("spriteid"));
                    this.sprites[i].setSpritememo((String) item.get("spritememo"));
                    this.sprites[i].setSpritemoney((Integer) item.get("spritemoney"));
                    this.sprites[i].setSpritename((String) item.get("spritename"));
                    this.sprites[i].setSpritesex((Integer) item.get("spritesex"));
                    this.sprites[i].setSpritestatus((Integer) item.get("spritestatus"));
                    this.sprites[i].setSpriteweary((Integer) item.get("spriteweary"));
                    sql = "select * from SpriteMaterial a left join Material b on a.materialid=b.materialid where a.spritematesite=2 and a.spriteid=" + this.sprites[i].getSpriteid();
                    result = DBProxy.QeurySelect(sql);
                    if (result != null && result.size() > 0) {
                        this.sprites[i].spriteMaterial = new PlaySpriteMaterial[result.size()];
                        for (int j = 0; j < result.size(); j++) {
                            item = result.get(j);
                            this.sprites[i].spriteMaterial[j] = new PlaySpriteMaterial();
                            PlaySpriteMaterial mt = this.sprites[i].spriteMaterial[j];
                            mt.setMaterialid((Long) item.get("materialid"));
                            mt.setSpriteid((Long) item.get("spriteid"));
                            mt.setSpritemateangle((Integer) item.get("spritemateangle"));
                            mt.setSpritematelayer((Integer) item.get("spritematelayer"));
                            mt.setSpritemateprice((Integer) item.get("spritemateprice"));
                            mt.setSpritematerialid((Long) item.get("spritematerialid"));
                            mt.setSpritemateroomid((Long) item.get("spritemateroomid"));
                            mt.setSpritematesite((Integer) item.get("spritematesite"));
                            mt.setSpritematex((Integer) item.get("spritematex"));
                            mt.setSpritematey((Integer) item.get("spritematey"));
                            mt.material.setMaterialcate((Integer) item.get("materialcate"));
                            mt.material.setMaterialcondition((String) item.get("materialcondition"));
                            mt.material.setMaterialid((Long) item.get("materialid"));
                            mt.material.setMateriallayer((Integer) item.get("materiallayer"));
                            mt.material.setMaterialmemo((String) item.get("materialmemo"));
                            mt.material.setMaterialname((String) item.get("materialname"));
                            mt.material.setMaterialprice((Integer) item.get("materialprice"));
                            mt.material.setMaterialspritecate((Integer) item.get("materialspritecate"));
                            mt.material.setMaterialvalue((Integer) item.get("materialvalue"));
                        }
                    }
                    sql = "select * from SpriteHome where spriteid=" + this.sprites[i].getSpriteid();
                    result = DBProxy.QeurySelect(sql);
                    if (result != null && result.size() > 0) {
                        this.sprites[i].spriteHome = new SpriteHome[result.size()];
                        for (int j = 0; j < result.size(); j++) {
                            item = result.get(j);
                            this.sprites[i].spriteHome[j] = new SpriteHome();
                            SpriteHome home = this.sprites[i].spriteHome[j];
                            home.setRoomid((Long) item.get("roomid"));
                            home.setSpritehomeid((Long) item.get("spritehomeid"));
                            home.setSpritehomememo((String) item.get("spritehomememo"));
                            home.setSpritehomename((String) item.get("spritehomename"));
                            home.setSpritehomeopen((Integer) item.get("spritehomeopen"));
                            home.setSpriteid((Long) item.get("spriteid"));
                        }
                    }
                }
            }
        }
    }
}
