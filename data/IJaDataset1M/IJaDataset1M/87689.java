package dao_service;

import java.util.ArrayList;
import Protocol.contact.Contact;
import Protocol.contact.ContactGroup;
import Protocol.contact.GmRecord;
import Protocol.user.BasePersonInfo;
import i_dao.I_Contact_Group_Dao;

public class ContactGroupImp implements I_Contact_Group_Dao {

    @Override
    public boolean insertGroup(ContactGroup group) {
        return false;
    }

    @Override
    public boolean updateGroup(ContactGroup group) {
        return false;
    }

    @Override
    public boolean deleteGroup(int group_id) {
        return false;
    }

    @Override
    public ContactGroup getGroup(int group_id) {
        return null;
    }

    @Override
    public ArrayList<ContactGroup> getGroupList(int owner_id) {
        return null;
    }

    @Override
    public boolean insertGmRecord(GmRecord record) {
        return false;
    }

    @Override
    public boolean deleteGmRecord(int record_id) {
        return false;
    }

    @Override
    public ArrayList<GmRecord> getGmRecords(int group_id) {
        return null;
    }

    @Override
    public Contact getContactByOwnerId(int user_id) {
        return null;
    }

    @Override
    public ArrayList<BasePersonInfo> getDefaultList() {
        return null;
    }
}
