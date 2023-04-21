package org.mati.geotech.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.ImageIcon;
import org.mati.geotech.model.GeoObject;

public class ObjectCatalog {

    private HashMap<String, HashMap<String, HashMap<String, String>>> _objCat = new HashMap<String, HashMap<String, HashMap<String, String>>>();

    private Vector<String> _typeId = new Vector<String>();

    private Vector<String> _catId = new Vector<String>();

    public ObjectCatalog() {
        _typeId.add("wrong type");
        _catId.add("wrong cat");
        addCategory("Геологоразведка");
        addObjectType("Геологоразведка", "Поиск нефтяных месторождений (геологоразведочные скважины)", defProp());
        setObjImage("Геологоразведка", "Поиск нефтяных месторождений (геологоразведочные скважины)", "s_o_h");
        addObjectType("Геологоразведка", "Поиск нефтяных месторождений (гидрогеологические скважины)", defProp());
        setObjImage("Геологоразведка", "Поиск нефтяных месторождений (гидрогеологические скважины)", "s_o_w");
        addObjectType("Геологоразведка", "Поиск нефтяных месторождений (инженерно-геологические скважины)", defProp());
        setObjImage("Геологоразведка", "Поиск нефтяных месторождений (инженерно-геологические скважины)", "s_o_e");
        addCategory("Бурение");
        addObjectType("Бурение", "Нефтяная скважина", defProp());
        setObjImage("Бурение", "Нефтяная скважина", "d_o");
        addObjectType("Бурение", "Газовая скважина", defProp());
        setObjImage("Бурение", "Газовая скважина", "d_g");
        addObjectType("Бурение", "Угольная шахта", defProp());
        setObjImage("Бурение", "Угольная шахта", "d_c_m");
        addObjectType("Бурение", "Угольный карьер", defProp());
        setObjImage("Бурение", "Угольный карьер", "d_c_p");
        addCategory("Добыча");
        addObjectType("Добыча", "Добыча нефти", defProp());
        setObjImage("Добыча", "Добыча нефти", "p_o");
        addObjectType("Добыча", "Добыча газа", defProp());
        setObjImage("Добыча", "Добыча газа", "p_g");
        addCategory("Переработка");
        addObjectType("Переработка", "Нефтеперерабатывающий завод", defProp());
        setObjImage("Переработка", "Нефтеперерабатывающий завод", "pz_o");
        addObjectType("Переработка", "Газоперерабатывающий завод", defProp());
        setObjImage("Переработка", "Газоперерабатывающий завод", "pz_g");
        addObjectType("Переработка", "Нефтеперерабатывающий завод с газоотбензинивающей установкой", defProp());
        setObjImage("Переработка", "Нефтеперерабатывающий завод с газоотбензинивающей установкой", "pz_go");
        addCategory("Транспортировка");
        addObjectType("Транспортировка", "Автотранспорт", defProp());
        setObjImage("Транспортировка", "Автотранспорт", "trans_lorry");
        addObjectType("Транспортировка", "Водный транспорт", defProp());
        setObjImage("Транспортировка", "Водный транспорт", "trans_ship");
        addObjectType("Транспортировка", "Железнодорожный транспорт", defProp());
        setObjImage("Транспортировка", "Железнодорожный транспорт", "trans_train");
        addObjectType("Транспортировка", "Трубопроводный транспорт", defProp());
        setObjImage("Транспортировка", "Трубопроводный транспорт", "trans_pipe");
        addCategory("Хранение");
        addObjectType("Хранение", "Хранилище нефти", defProp());
        setObjImage("Хранение", "Хранилище нефти", "st_oil");
        addObjectType("Хранение", "Хранилище газа", defProp());
        setObjImage("Хранение", "Хранилище газа", "st_gas");
    }

    private HashMap<String, String> defProp() {
        HashMap<String, String> prop = new HashMap<String, String>();
        prop.put("filtred", "false");
        prop.put("image", "default");
        return prop;
    }

    public void addCategory(String catName) {
        if (!_objCat.keySet().contains(catName)) {
            HashMap<String, HashMap<String, String>> newCat = new HashMap<String, HashMap<String, String>>();
            _objCat.put(catName, newCat);
            _catId.add(catName);
        }
    }

    public Set<String> getCategoris() {
        return _objCat.keySet();
    }

    public void addObjectType(String cat, String name, HashMap<String, String> props) {
        _typeId.add(name);
        if (!_objCat.keySet().contains(cat)) {
            addCategory(cat);
        }
        _objCat.get(cat).put(name, props);
    }

    public Map<String, String> getObjectProps(String catName, String objName) {
        if (!_objCat.keySet().contains(catName)) {
            return null;
        }
        return _objCat.get(catName).get(objName);
    }

    public Set<String> getObjectNames(String catName) {
        return _objCat.get(catName).keySet();
    }

    public ImageIcon getObjImage(String catName, String objName) {
        return Images.getImageIcon(getObjectProps(catName, objName).get("image"));
    }

    public void setObjImage(String catName, String objName, String imageName) {
        getObjectProps(catName, objName).put("image", imageName);
    }

    public GeoObject create(String string, String objTypeName) {
        GeoObject go = new GeoObject();
        go.getProps().put("lon", "0");
        go.getProps().put("lat", "0");
        go.setStartLvl(0);
        System.out.println("create: " + String.valueOf(_typeId.indexOf(objTypeName)) + " - " + objTypeName);
        go.getProps().put("type", String.valueOf(_typeId.indexOf(objTypeName)));
        return go;
    }

    public int getTypeId(String objTypeName) {
        return _typeId.indexOf(objTypeName);
    }

    public boolean isTypeFiltred(int type) {
        String name = _typeId.get(type);
        for (String title : getCategoris()) {
            if (getObjectProps(title, name) != null && Boolean.parseBoolean(getObjectProps(title, name).get("filtred"))) {
                return true;
            }
        }
        return false;
    }
}
