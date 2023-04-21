package jule.clases;

import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class table {

    private String _nombre;

    public String getName() {
        return _nombre;
    }

    public void setName(String d) {
        _nombre = d;
    }

    public String getTargetName() {
        return _targetName;
    }

    public void setTargetName(String _targetName) {
        this._targetName = _targetName;
    }

    private String _targetName;

    private ArrayList<field> _fields;

    public ArrayList<field> getFields() {
        return _fields;
    }

    public void setFields(ArrayList<field> d) {
        _fields = d;
    }

    public int getNumberOfFields() {
        return _fields.size();
    }

    private ArrayList<field> _fieldsKey;

    public ArrayList<field> getFieldsKey() {
        return _fieldsKey;
    }

    public void setFieldsKey(ArrayList<field> d) {
        _fieldsKey = d;
    }

    public String getKey() {
        try {
            String valor = "";
            valor = this.getFieldsKey().get(0).getNombre();
            return (valor);
        } catch (Exception ex) {
            return "id" + _nombre;
        }
    }

    public String getFieldDescription() {
        try {
            String valor = "";
            valor = this.getFieldsWithoutKeys().get(0).getNombre();
            return (valor);
        } catch (Exception ex) {
            return "id" + _nombre;
        }
    }

    private ArrayList<field> _fieldsWithoutKeys;

    public ArrayList<field> getFieldsWithoutKeys() {
        return _fieldsWithoutKeys;
    }

    public void setFieldsWithoutKeys(ArrayList<field> d) {
        _fieldsWithoutKeys = d;
    }

    public String toString() {
        return this.getName();
    }

    private Boolean _selected;

    public Boolean getSelected() {
        return _selected;
    }

    public void setSelected(Boolean d) {
        _selected = d;
    }

    private String _listaTodosLosCampos;

    public String getListWithAllFields() {
        return _listaTodosLosCampos;
    }

    public void setListWithAllTheFields(String d) {
        _listaTodosLosCampos = d;
    }

    private String _listaTodosLosCamposSinCamposClave;

    public String getlistWithAllFieldsExceptKeys() {
        return _listaTodosLosCamposSinCamposClave;
    }

    public void setlistaTodosLosCamposSinCamposClave(String d) {
        _listaTodosLosCamposSinCamposClave = d;
    }

    private String _listaSoloCamposClave;

    public String getListOnlyKeyFields() {
        return _listaSoloCamposClave;
    }

    public void setlistaSoloCamposClave(String d) {
        _listaSoloCamposClave = d;
    }

    private String _listaParametroCamposClave;

    public String getListParameters() {
        return _listaParametroCamposClave;
    }

    public void setListParameters(String d) {
        _listaParametroCamposClave = d;
    }

    private String _ListParametersKeyFieldForPhp;

    public String getListParametersForPhp() {
        return _ListParametersKeyFieldForPhp;
    }

    public void setListParametersForPhp(String d) {
        _ListParametersKeyFieldForPhp = d;
    }

    private String _ListParametersKeyFieldForCsharp;

    public String getListParametersForCsharp() {
        return _ListParametersKeyFieldForCsharp;
    }

    public void setListParametersForCsharp(String d) {
        _ListParametersKeyFieldForCsharp = d;
    }

    private String listFieldsQuoted;

    public String getListFieldsWithQuotes() {
        return listFieldsQuoted;
    }

    public void setListFieldsWithQuotes(String d) {
        listFieldsQuoted = d;
    }

    private String listAllFieldsWithQuotes;

    public String getListAllFieldsWithQuotes() {
        return listAllFieldsWithQuotes;
    }

    public void setListAllFieldsWithQuotes(String d) {
        listAllFieldsWithQuotes = d;
    }

    private String _ListNewObject;

    public String getListNewObject() {
        return _ListNewObject;
    }

    public void set_ListNewObject(String d) {
        _ListNewObject = d;
    }

    private ArrayList<relation> _relations = new ArrayList<relation>();

    public ArrayList<relation> getRelations() {
        return _relations;
    }

    public void setrelations(ArrayList<relation> d) {
        _relations = d;
    }

    public void addRelation(relation d) {
        _relations.add(d);
    }
}
