package de.beas.explicanto.client.sec.jaxb.impl;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;
import de.beas.explicanto.client.sec.jaxb.Character;
import de.beas.explicanto.client.sec.properties.CharacterPS;

public class CharacterImpl implements Character, IAdaptable {

    protected java.lang.String _Sex;

    protected java.lang.String _Contribution;

    protected java.lang.String _Color;

    protected int _Age;

    protected java.lang.String _Language;

    protected java.lang.String _FacialExpression;

    protected java.lang.String _Gestures;

    protected java.lang.String _Size;

    protected java.lang.String _Nickname;

    protected java.lang.String _Name;

    protected java.lang.String _Role;

    protected java.lang.String _Clothing;

    protected long _Uid;

    public java.lang.String getSex() {
        return _Sex;
    }

    public void setSex(java.lang.String value) {
        _Sex = value;
    }

    public java.lang.String getContribution() {
        return _Contribution;
    }

    public void setContribution(java.lang.String value) {
        _Contribution = value;
    }

    public java.lang.String getColor() {
        return _Color;
    }

    public void setColor(java.lang.String value) {
        _Color = value;
    }

    public int getAge() {
        return _Age;
    }

    public void setAge(int value) {
        _Age = value;
    }

    public java.lang.String getLanguage() {
        return _Language;
    }

    public void setLanguage(java.lang.String value) {
        _Language = value;
    }

    public java.lang.String getFacialExpression() {
        return _FacialExpression;
    }

    public void setFacialExpression(java.lang.String value) {
        _FacialExpression = value;
    }

    public java.lang.String getGestures() {
        return _Gestures;
    }

    public void setGestures(java.lang.String value) {
        _Gestures = value;
    }

    public java.lang.String getSize() {
        return _Size;
    }

    public void setSize(java.lang.String value) {
        _Size = value;
    }

    public java.lang.String getNickname() {
        return _Nickname;
    }

    public void setNickname(java.lang.String value) {
        _Nickname = value;
    }

    public java.lang.String getName() {
        return _Name;
    }

    public void setName(java.lang.String value) {
        _Name = value;
    }

    public java.lang.String getRole() {
        return _Role;
    }

    public void setRole(java.lang.String value) {
        _Role = value;
    }

    public java.lang.String getClothing() {
        return _Clothing;
    }

    public void setClothing(java.lang.String value) {
        _Clothing = value;
    }

    public long getUid() {
        return _Uid;
    }

    public void setUid(long value) {
        _Uid = value;
    }

    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySource.class) return new CharacterPS(this);
        return null;
    }
}
