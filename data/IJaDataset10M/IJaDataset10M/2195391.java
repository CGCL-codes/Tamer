package org.beanstalk4j.model.builder;

import java.util.Date;
import org.beanstalk4j.model.PublicKey;
import org.beanstalk4j.utils.IsoDateTimeFormat;
import org.beanstalk4j.xml.DOMUtils;
import org.w3c.dom.Element;

public class PublicKeyBuilder {

    private final PublicKey publicKey;

    public PublicKeyBuilder() {
        publicKey = new PublicKey();
    }

    public PublicKeyBuilder(Element element) {
        this();
        id(DOMUtils.getChildText(element, "id"));
        accountId(DOMUtils.getChildText(element, "account-id"));
        userId(DOMUtils.getChildText(element, "user-id"));
        name(DOMUtils.getChildText(element, "name"));
        content(DOMUtils.getChildText(element, "content"));
        createdAt(DOMUtils.getChildText(element, "created-at"));
        updatedAt(DOMUtils.getChildText(element, "updated-at"));
    }

    public PublicKeyBuilder id(Integer id) {
        publicKey.setId(id);
        return this;
    }

    public PublicKeyBuilder id(String id) {
        return id(Integer.valueOf(id));
    }

    public PublicKeyBuilder accountId(Integer accountId) {
        publicKey.setAccountId(accountId);
        return this;
    }

    public PublicKeyBuilder accountId(String accountId) {
        return accountId(Integer.valueOf(accountId));
    }

    public PublicKeyBuilder userId(Integer userId) {
        publicKey.setUserId(userId);
        return this;
    }

    public PublicKeyBuilder userId(String userId) {
        return userId(Integer.valueOf(userId));
    }

    public PublicKeyBuilder name(String name) {
        publicKey.setName(name);
        return this;
    }

    public PublicKeyBuilder content(String content) {
        publicKey.setContent(content);
        return this;
    }

    public PublicKeyBuilder createdAt(Date createdAt) {
        publicKey.setCreatedAt(createdAt);
        return this;
    }

    public PublicKeyBuilder createdAt(String createdAt) {
        return createdAt(IsoDateTimeFormat.parse(createdAt));
    }

    public PublicKeyBuilder updatedAt(Date updatedAt) {
        publicKey.setUpdatedAt(updatedAt);
        return this;
    }

    public PublicKeyBuilder updatedAt(String updatedAt) {
        return updatedAt(IsoDateTimeFormat.parse(updatedAt));
    }

    public PublicKey build() {
        return publicKey;
    }
}
