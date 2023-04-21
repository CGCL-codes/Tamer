package cn.com.believer.songyuanframework.openapi.storage.box.impl.simple.utils;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import cn.com.believer.songyuanframework.openapi.storage.box.constant.BoxConstant;
import cn.com.believer.songyuanframework.openapi.storage.box.factories.BoxObjectFactory;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.Box;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.BoxFile;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.BoxFolder;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.BoxFriend;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.BoxSubscription;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.BoxTag;

/**
 * @author Jimmy
 * 
 */
public final class ConverterUtils {

    /**
     * 
     */
    private ConverterUtils() {
    }

    /**
     * convert tags element to tag list.
     * 
     * @param tagsElm
     *            tags element
     * @return object list
     */
    public static List transferTags2List(Element tagsElm) {
        List tagsList = new ArrayList();
        for (int i = 0; i < tagsElm.nodeCount(); i++) {
            Element tagElm = (Element) tagsElm.node(i);
            String id = tagElm.attributeValue("id");
            String name = tagElm.getText();
            BoxTag tag = BoxObjectFactory.createBoxTag();
            tag.setId(id);
            tag.setName(name);
            tagsList.add(tag);
        }
        return tagsList;
    }

    /**
     * 
     * @param folderElm
     *            folder element
     * @return object
     */
    public static BoxFolder toBoxFolder(Element folderElm) {
        BoxFolder boxFolder = BoxObjectFactory.createBoxFolder();
        Element folderIdElm = folderElm.element(BoxConstant.PARAM_NAME_FOLDER_ID);
        Element folderNameElm = folderElm.element(BoxConstant.PARAM_NAME_FOLDER_NAME);
        Element folderTypeIdElm = folderElm.element(BoxConstant.PARAM_NAME_FOLDER_TYPE_ID);
        Element parentFolderIdElm = folderElm.element(BoxConstant.PARAM_NAME_PARENT_FOLDER_ID);
        Element userIdElm = folderElm.element(BoxConstant.PARAM_NAME_USER_ID);
        Element pathElm = folderElm.element(BoxConstant.PARAM_NAME_PATH);
        Element sharedElm = folderElm.element(BoxConstant.PARAM_NAME_SHARED);
        Element publicNameElm = folderElm.element(BoxConstant.PARAM_NAME_PUBLIC_NAME);
        Element showCommentsElm = folderElm.element(BoxConstant.PARAM_NAME_SHOW_COMMENTS);
        Element passwordElm = folderElm.element(BoxConstant.PARAM_NAME_PASSWORD);
        boxFolder.setFolderId(folderIdElm.getText());
        boxFolder.setFolderName(folderNameElm.getText());
        if (folderTypeIdElm != null) {
            boxFolder.setFolderTypeId(folderTypeIdElm.getText());
        }
        boxFolder.setParentFolderId(parentFolderIdElm.getText());
        if (passwordElm != null) {
            boxFolder.setPassword(passwordElm.getText());
        }
        if (pathElm != null) {
            boxFolder.setPath(pathElm.getText());
        }
        if (publicNameElm != null) {
            boxFolder.setPublicName(publicNameElm.getText());
        }
        if (sharedElm != null) {
            boxFolder.setShared(sharedElm.getText());
        }
        if (showCommentsElm != null) {
            boxFolder.setShowComments(showCommentsElm.getText());
        }
        if (userIdElm != null) {
            boxFolder.setUserId(userIdElm.getText());
        }
        return boxFolder;
    }

    /**
     * @param infoElm
     *            info element
     * @return object
     */
    public static BoxFile toBoxFile(Element infoElm) {
        BoxFile boxFile = BoxObjectFactory.createBoxFile();
        Element fileIdElm = infoElm.element(BoxConstant.PARAM_NAME_FILE_ID);
        Element fileNameElm = infoElm.element(BoxConstant.PARAM_NAME_FILE_NAME);
        Element folderIdElm = infoElm.element(BoxConstant.PARAM_NAME_FOLDER_ID);
        Element sharedElm = infoElm.element(BoxConstant.PARAM_NAME_SHARED);
        Element sharedNameElm = infoElm.element(BoxConstant.PARAM_NAME_SHARED_NAME);
        Element sizeElm = infoElm.element(BoxConstant.PARAM_NAME_SIZE);
        Element descriptionElm = infoElm.element(BoxConstant.PARAM_NAME_DESCRIPTION);
        Element sha1Elm = infoElm.element(BoxConstant.PARAM_NAME_SHA1);
        Element createdElm = infoElm.element(BoxConstant.PARAM_NAME_CREATED);
        Element updatedElm = infoElm.element(BoxConstant.PARAM_NAME_UPDATED);
        if (fileIdElm != null) {
            boxFile.setFileId(fileIdElm.getText());
        }
        if (fileNameElm != null) {
            boxFile.setFileName(fileNameElm.getText());
        }
        if (folderIdElm != null) {
            boxFile.setFolderId(folderIdElm.getText());
        }
        if (sharedElm != null) {
            String sharedStr = sharedElm.getText();
            if ("0".equals(sharedStr)) {
                boxFile.setShared(false);
            } else {
                boxFile.setShared(true);
            }
        }
        if (sharedNameElm != null) {
            boxFile.setSharedName(sharedNameElm.getText());
        }
        if (sizeElm != null) {
            long size = 0;
            String sizeStr = sizeElm.getText();
            try {
                size = Long.parseLong(sizeStr);
            } catch (NumberFormatException e) {
                size = Long.MIN_VALUE;
            }
            boxFile.setSize(size);
        }
        if (descriptionElm != null) {
            boxFile.setDescription(descriptionElm.getText());
        }
        if (sha1Elm != null) {
            boxFile.setSha1(sha1Elm.getText());
        }
        if (createdElm != null) {
            long created = 0;
            String createdStr = createdElm.getText();
            try {
                created = Long.parseLong(createdStr);
            } catch (NumberFormatException e) {
                created = Long.MIN_VALUE;
            }
            boxFile.setCreated(created);
        }
        if (updatedElm != null) {
            long updated = 0;
            String updatedStr = updatedElm.getText();
            try {
                updated = Long.parseLong(updatedStr);
            } catch (NumberFormatException e) {
                updated = Long.MIN_VALUE;
            }
            boxFile.setUpdated(updated);
        }
        return boxFile;
    }

    /**
     * @param friendsElm
     *            friends element
     * @return object list
     */
    public static List toFriendsList(Element friendsElm) {
        List friendsList = new ArrayList();
        if (friendsElm != null) {
            for (int i = 0; i < friendsElm.nodeCount(); i++) {
                BoxFriend friend = BoxObjectFactory.createBoxFriend();
                Element friendElm = (Element) friendsElm.node(i);
                Element nameElm = friendElm.element("name");
                Element emailElm = friendElm.element("email");
                Element acceptedElm = friendElm.element("accepted");
                Element avatarUrlElm = friendElm.element("avatar_url");
                Element boxesElm = friendElm.element("boxes");
                Element subscriptionsElm = friendElm.element("subscriptions");
                if (nameElm != null) {
                    friend.setName(nameElm.getText());
                }
                if (emailElm != null) {
                    friend.setEmail(emailElm.getText());
                }
                if (acceptedElm != null) {
                    friend.setAccepted(acceptedElm.getText());
                }
                if (avatarUrlElm != null) {
                    friend.setAvatarUrl(avatarUrlElm.getText());
                }
                List boxList = new ArrayList();
                if (boxesElm != null) {
                    for (int j = 0; j < boxesElm.nodeCount(); j++) {
                        Element boxElm = (Element) boxesElm.node(j);
                        Box box = toBox(boxElm);
                        boxList.add(box);
                    }
                }
                friend.setBoxes(boxList);
                List subscriptionList = new ArrayList();
                if (subscriptionsElm != null) {
                    for (int j = 0; j < subscriptionsElm.nodeCount(); j++) {
                        Element subscriptionElm = (Element) subscriptionsElm.node(j);
                        BoxSubscription subscription = toSubscription(subscriptionElm);
                        subscriptionList.add(subscription);
                    }
                }
                friend.setSubscriptions(subscriptionList);
                friendsList.add(friend);
            }
        }
        return friendsList;
    }

    /**
     * 
     * @param boxElm
     *            box element
     * @return object
     */
    public static Box toBox(Element boxElm) {
        Box box = BoxObjectFactory.createBox();
        Element idElm = boxElm.element("id");
        Element urlElm = boxElm.element("url");
        Element statusElm = boxElm.element("status");
        if (idElm != null) {
            box.setId(idElm.getText());
        }
        if (urlElm != null) {
            box.setUrl(urlElm.getText());
        }
        if (statusElm != null) {
            box.setStatus(statusElm.getText());
        }
        return box;
    }

    /**
     * 
     * @param subscriptionElm
     *            subscription element
     * @return object
     */
    public static BoxSubscription toSubscription(Element subscriptionElm) {
        BoxSubscription subscription = BoxObjectFactory.createBoxSubscription();
        Element boxIdElm = subscriptionElm.element("box_id");
        Element userNameElm = subscriptionElm.element("user_name");
        Element urlElm = subscriptionElm.element("url");
        Element statusElm = subscriptionElm.element("status");
        if (boxIdElm != null) {
            subscription.setBoxId(boxIdElm.getText());
        }
        if (userNameElm != null) {
            subscription.setUserName(userNameElm.getText());
        }
        if (urlElm != null) {
            subscription.setUrl(urlElm.getText());
        }
        if (statusElm != null) {
            subscription.setStatus(statusElm.getText());
        }
        return subscription;
    }
}
