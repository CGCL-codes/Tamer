package net.sf.webwarp.modules.cancellation.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import net.sf.webwarp.modules.cancellation.CancellationEvent;
import net.sf.webwarp.modules.cancellation.CancellationList;
import net.sf.webwarp.modules.cancellation.CancellationListConfig;
import net.sf.webwarp.modules.cancellation.CancellationListProvider;
import net.sf.webwarp.modules.cancellation.MaxTriesExceededException;
import net.sf.webwarp.modules.cancellation.NoMoreListItemsException;
import net.sf.webwarp.modules.cancellation.UserAlreadyExistsException;
import net.sf.webwarp.modules.cancellation.UserNotExistsException;
import net.sf.webwarp.modules.cancellation.ValidationContext;
import net.sf.webwarp.util.crypt.TripleDESBean;
import net.sf.webwarp.util.hibernate.dao.Preload;
import net.sf.webwarp.util.hibernate.dao.impl.BaseHibernateDAOImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CancellationListProviderImpl extends BaseHibernateDAOImpl implements CancellationListProvider {

    private TripleDESBean tripleDES;

    private Integer automaticlyGenerateNewSize = 10;

    private Integer maxTries = 3;

    private Transformer encrypter = new Transformer() {

        public Object transform(Object input) {
            return tripleDES.encrypt((String) input);
        }
    };

    private Transformer decrypter = new Transformer() {

        public Object transform(Object input) {
            return tripleDES.decrypt((String) input);
        }
    };

    private SecureRandom random;

    private ApplicationEventPublisher publisher;

    public CancellationListProviderImpl() {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public UserCancellationList getUserCancellationList(String userID) {
        return this.getUserList(userID);
    }

    public CancellationList getActualList(String userID) {
        UserCancellationList userList = getUserList(userID);
        if (userList != null) {
            return decrypt(userList.getCurrent());
        }
        return null;
    }

    public CancellationList getNewList(String userID) {
        UserCancellationList userList = getUserList(userID);
        if (userList != null && userList.getNext() != null) {
            return decrypt(userList.getNext());
        }
        return null;
    }

    public boolean hasNewList(String userID) {
        UserCancellationList userList = getUserList(userID);
        if (userList != null) {
            return userList.getNext() != null;
        }
        return false;
    }

    public boolean existsList(String userID) {
        return getUserList(userID) != null;
    }

    public CancellationList createList(String userID, CancellationListConfig config) throws UserAlreadyExistsException {
        if (existsList(userID)) {
            throw new UserAlreadyExistsException(userID);
        }
        UserCancellationList userList = new UserCancellationList(userID);
        CancellationList cancellationList = ListGenerator.generateList(config, userList.getLastListNumber() + 1);
        userList.appendNewList(encrypt(cancellationList));
        saveUserList(userList);
        publishEvent(new CancellationEvent(this, userID, CancellationEvent.EventType.createList, cancellationList.getListNumber()));
        return cancellationList;
    }

    public CancellationList getList(String userID, Integer listNumber) throws UserNotExistsException {
        UserCancellationList userList = getUserList(userID);
        if (userList != null) {
            CancellationList cancellationList = userList.getList(listNumber);
            if (cancellationList == null) {
                throw new IllegalArgumentException("user: " + userID + " has no cancellation list");
            }
            return decrypt(cancellationList);
        }
        throw new UserNotExistsException(userID);
    }

    public CancellationList replaceList(String userID, CancellationListConfig config) throws UserNotExistsException {
        UserCancellationList userList = getUserList(userID);
        if (userList == null) {
            throw new UserNotExistsException(userID);
        }
        CancellationList cancellationList = ListGenerator.generateList(config, userList.getLastListNumber() + 1);
        userList.replaceList(encrypt(cancellationList));
        publishEvent(new CancellationEvent(this, userID, CancellationEvent.EventType.replaceList, cancellationList.getListNumber()));
        return cancellationList;
    }

    public boolean hasMoreTries(String userID) {
        UserCancellationList userList = getUserList(userID);
        if (userList == null) {
            return false;
        }
        return userList.getCurrent().getNumTries() < getMaxTries();
    }

    public void augmentNumTries(String userID, Integer augment) {
        UserCancellationList userList = getUserList(userID);
        if (userList != null) {
            userList.getCurrent().setNumTries(userList.getCurrent().getNumTries() + augment);
        }
    }

    public Integer getNumTries(String userID) {
        UserCancellationList userList = getUserList(userID);
        if (userList != null) {
            return userList.getCurrent().getNumTries();
        }
        return null;
    }

    public void resetNumTries(String userID) {
        UserCancellationList userList = getUserList(userID);
        if (userList != null) {
            userList.getCurrent().setNumTries(0);
        }
    }

    public ValidationContext createValidationContext(String userID) throws MaxTriesExceededException, UserNotExistsException, NoMoreListItemsException {
        UserCancellationList userList = getUserList(userID);
        if (userList == null) {
            throw new UserNotExistsException(userID);
        }
        CancellationList cancellationList = userList.getCurrent();
        if (cancellationList == null) {
            throw new IllegalArgumentException("user: " + userID + " has no cancellation list");
        }
        if (!(userList.getCurrent().getNumTries() < getMaxTries())) {
            throw new MaxTriesExceededException(userID);
        }
        int totalSize = cancellationList.getItems().size();
        int usedSize = cancellationList.getUsedPositions().size();
        boolean generatedNew = false;
        if (totalSize == usedSize) {
            if (userList.getNext() == null) {
                throw new NoMoreListItemsException(userID);
            }
            userList.moveNextToCurrent();
            cancellationList = userList.getCurrent();
            totalSize = cancellationList.getItems().size();
            usedSize = cancellationList.getUsedPositions().size();
        } else {
            if (totalSize - usedSize <= automaticlyGenerateNewSize) {
                if (userList.getNext() == null) {
                    generatedNew = true;
                    CancellationList newCancellationList = ListGenerator.generateList(cancellationList.getListConfig(), userList.getLastListNumber() + 1);
                    userList.appendNewList(encrypt(newCancellationList));
                    publishEvent(new CancellationEvent(this, userList.getUserID(), CancellationEvent.EventType.appendList, newCancellationList.getListNumber()));
                }
            }
        }
        Integer nextPosition = getNextListPosition(cancellationList, totalSize - usedSize);
        return new ValidationContextImpl(userID, cancellationList.getListNumber(), nextPosition, generatedNew);
    }

    public boolean validateUser(ValidationContext validationContext) throws UserNotExistsException {
        if (validationContext == null || validationContext.getUserID() == null || validationContext.getListItem() == null) {
            return false;
        }
        UserCancellationList userList = getUserList(validationContext.getUserID());
        if (userList != null) {
            CancellationList cancellationList = userList.getList(validationContext.getListNumber());
            if (cancellationList != null) {
                Integer listPosition = validationContext.getListPosition() - 1;
                String decrypted = tripleDES.decrypt(cancellationList.getItems().get(listPosition));
                String incomingItem = validationContext.getListItem();
                if (cancellationList.getListConfig().getOnlyUpperCase()) {
                    incomingItem = incomingItem.toUpperCase();
                    decrypted = decrypted.toUpperCase();
                }
                if (incomingItem.equals(decrypted)) {
                    userList.getCurrent().setNumTries(0);
                    return true;
                }
            } else {
                throw new IllegalArgumentException("user: " + validationContext.getUserID() + " has no cancellation list");
            }
            userList.getCurrent().setNumTries(userList.getCurrent().getNumTries() + 1);
        } else {
            throw new UserNotExistsException(validationContext.getUserID());
        }
        return false;
    }

    public void removeList(String userID) {
        super.delete(getUserList(userID));
    }

    @SuppressWarnings("unchecked")
    public List<String> getNumTriesExceededUsernames() {
        return getSession().createQuery("select distinct u.userID from " + UserCancellationList.class.getSimpleName() + " u where u.cancellationList1.numTries >= :numTries").setParameter("numTries", maxTries).list();
    }

    public void updateUsername(String oldUsername, String newUsernae) {
        UserCancellationList userList = getUserList(oldUsername);
        userList.setUserID(newUsernae);
    }

    private void publishEvent(CancellationEvent event) {
        publisher.publishEvent(event);
    }

    private Integer getNextListPosition(CancellationList cancellationList, int leftSize) {
        List<Integer> leftPositions = new ArrayList<Integer>(leftSize);
        for (int i = 0; i < cancellationList.getListConfig().getListSize(); i++) {
            if (!cancellationList.getUsedPositions().contains(i)) {
                leftPositions.add(i);
            }
        }
        int nextRandom = random.nextInt(leftSize);
        Integer nextPosition = leftPositions.get(nextRandom);
        cancellationList.getUsedPositions().add(nextPosition);
        return nextPosition + 1;
    }

    private CancellationList encrypt(CancellationList cancellationList) {
        CancellationList clone = (CancellationList) cancellationList.clone();
        CollectionUtils.transform(clone.getItems(), encrypter);
        return clone;
    }

    private CancellationList decrypt(CancellationList cancellationList) {
        CancellationList clone = (CancellationList) cancellationList.clone();
        CollectionUtils.transform(clone.getItems(), decrypter);
        return clone;
    }

    private void saveUserList(UserCancellationList userCancellationList) {
        super.save(userCancellationList);
    }

    private UserCancellationList getUserList(String userID) {
        return (UserCancellationList) super._executeUniqueNamedQuery("CancellationList.byUserID", "userID", userID, new Preload("cancellationList1.items"), new Preload("cancellationList2.items"));
    }

    public Integer getAutomaticlyGenerateNewSize() {
        return automaticlyGenerateNewSize;
    }

    public void setAutomaticlyGenerateNewSize(Integer automaticlyGenerateNewSize) {
        this.automaticlyGenerateNewSize = automaticlyGenerateNewSize;
    }

    public Integer getMaxTries() {
        return maxTries;
    }

    public void setMaxTries(Integer maxTries) {
        this.maxTries = maxTries;
    }

    public void setTripleDES(TripleDESBean tripleDES) {
        this.tripleDES = tripleDES;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
