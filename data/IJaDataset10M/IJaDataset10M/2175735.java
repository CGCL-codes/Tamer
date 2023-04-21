package ces.coffice.webmail.facade;

import java.util.*;
import ces.coffice.webmail.datamodel.dao.*;
import ces.coffice.webmail.datamodel.dao.hibernate.*;
import ces.coffice.webmail.datamodel.vo.*;
import ces.coffice.webmail.ui.form.*;
import ces.coffice.webmail.util.*;
import ces.coral.log.Logger;

public class AddressFacade {

    Logger logger = new Logger(AddressFacade.class);

    public static final String PUBLIC_ADDRESS = "1";

    public static final String PRIVATE_ADDRESS = "0";

    public static final String PRIVATE_DIR = "6";

    public static final String PUBLIC_DIR = "7";

    /**
     * ���û���һ���ļ����´���һ����ַ��
     * @param mailAddress MailAddress
     * @throws Exception �����?���׳��쳣
     */
    protected void createAddress(MailAddress mailAddress) throws Exception {
        logger.debug("createAddress(MailAddress mailAddress)");
        if (mailAddress == null) {
            return;
        }
        mailAddress.setId(new SystemUtil().generateAddressId().longValue());
        AddressBookDao addressBookDao = new AddressBookDaoHibernate();
        addressBookDao.addMailAddress(mailAddress);
    }

    /**
     * ������ַ��
     * @param addressForm AddressForm
     * @throws Exception
     */
    public void createAddress(AddressForm addressForm) throws Exception {
        logger.debug("createAddress================");
        if (addressForm == null) {
            return;
        }
        MailAddress mailAddress = new MailAddress();
        mailAddress.setCompanyAddress(addressForm.getCompanyaddress());
        mailAddress.setCompanyName(addressForm.getCompanyname());
        mailAddress.setFamilyAddress(addressForm.getHomeaddress());
        mailAddress.setId(Long.parseLong(addressForm.getId()));
        mailAddress.setMailAddress(addressForm.getEmail());
        mailAddress.setUserId(Long.parseLong(addressForm.getUserid()));
        mailAddress.setMobilePhone(addressForm.getMobile());
        mailAddress.setDirectoryId(addressForm.getDirectoryId());
        mailAddress.setName(addressForm.getName());
        mailAddress.setNickname(addressForm.getNickname());
        mailAddress.setOwnFlag(addressForm.getOwnFlag());
        mailAddress.setPosition(addressForm.getPosition());
        mailAddress.setSurname(addressForm.getSurname());
        mailAddress.setTelephone(addressForm.getTelephone());
        this.createAddress(mailAddress);
    }

    /**
     * ��ѡ���Ŀ¼�½�����ַ����Ϣ
     * @param addressForm AddressForm
     * @param long directID
     * @throws Exception
     */
    public void createAddressbyID(AddressForm addressForm, long directID) {
        try {
            MailAddress mailAddress = new MailAddress();
            if (mailAddress.getDirectoryId() == directID) {
                this.createAddress(addressForm);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * �޸��û��ļ����µ�һ����ַ����Ŀ
     * @param  mailAddress MailAddress
     * @throws Exception �����?���׳��쳣
     */
    protected void modifyAddress(MailAddress mailAddress) throws Exception {
        if (mailAddress == null) {
            return;
        }
        AddressBookDao addressBookDao = new AddressBookDaoHibernate();
        addressBookDao.updateMailAddress(mailAddress);
    }

    /**
     * �޸ĵ�ַ��
     * @param addressForm AddressForm
     * @throws Exception
     */
    public void modifyAddress(AddressForm addressForm) throws Exception {
        if (addressForm == null) {
            return;
        }
        MailAddress mailAddress = new MailAddress();
        mailAddress.setCompanyAddress(addressForm.getCompanyaddress());
        mailAddress.setCompanyName(addressForm.getCompanyname());
        mailAddress.setFamilyAddress(addressForm.getHomeaddress());
        mailAddress.setId(Long.parseLong(addressForm.getId()));
        mailAddress.setMailAddress(addressForm.getEmail());
        mailAddress.setUserId(Long.parseLong(addressForm.getUserid()));
        mailAddress.setMobilePhone(addressForm.getMobile());
        mailAddress.setName(addressForm.getName());
        mailAddress.setNickname(addressForm.getNickname());
        mailAddress.setOwnFlag(addressForm.getOwnFlag());
        mailAddress.setPosition(addressForm.getPosition());
        mailAddress.setSurname(addressForm.getSurname());
        mailAddress.setTelephone(addressForm.getTelephone());
        mailAddress.setDirectoryId(addressForm.getDirectoryId());
        this.modifyAddress(mailAddress);
    }

    /**
     * ɾ���û��ļ����µ�һ����ַ����Ŀ����������ش���
     * @param mailAddress MailAddress
     * @throws Exception �����?���׳��쳣
     */
    protected void deleteAddress(MailAddress mailAddress) throws Exception {
        if (mailAddress == null) {
            return;
        }
        AddressBookDao addressBookDao = new AddressBookDaoHibernate();
        addressBookDao.removeMailAddress(mailAddress);
    }

    /**
     * ���IDɾ���ַ��
     * @param id String����ַ��ID
     * @throws Exception
     */
    public void deleteAddress(String id) throws Exception {
        MailAddress mailAddress = new MailAddress();
        mailAddress.setId(Long.parseLong(id));
        this.deleteAddress(mailAddress);
    }

    /**
     * ���Id���һ����ַ����Ŀ����
     * @param id long
     * @throws Exception �����?���׳��쳣
     * @return MailAddress
     */
    protected MailAddress getAddressById(long id) throws Exception {
        AddressBookDao addressBookDao = new AddressBookDaoHibernate();
        return addressBookDao.findById(id);
    }

    public AddressForm getAddressFormById(long id) throws Exception {
        MailAddress address = this.getAddressById(id);
        if (address == null) {
            return null;
        }
        return this.convertVoToForm(address);
    }

    /**
     * ����û������е�ַ����Ŀ����
     * @param mailUser MailUser
     * @throws Exception �����?���׳��쳣
     * @return Collection
     */
    protected Collection getAllOfUser(long userid) throws Exception {
        MailUser mailUser = new MailUser();
        mailUser.setId(new Long(userid));
        return new UserDaoHibernate(mailUser).getAddresses();
    }

    /**
     * �õ��û������е�ַ��
     * @param userid long���û�ID
     * @throws Exception
     * @return Collection
     */
    public Collection getAllAddressOfUser(long userid) throws Exception {
        Collection collection = this.getAllOfUser(userid);
        Collection addresses = new Vector();
        if (collection != null) {
            Iterator it = collection.iterator();
            if (it == null) {
                return addresses;
            }
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof MailAddress) {
                    AddressForm form = this.convertVoToForm((MailAddress) o);
                    form.setCompanyaddress(((MailAddress) o).getCompanyAddress());
                    form.setCompanyname(((MailAddress) o).getCompanyName());
                    form.setEmail(((MailAddress) o).getMailAddress());
                    form.setId(String.valueOf(((MailAddress) o).getId()));
                    form.setDirectoryId(((MailAddress) o).getDirectoryId());
                    form.setHomeaddress(((MailAddress) o).getFamilyAddress());
                    form.setMobile(((MailAddress) o).getMobilePhone());
                    form.setName(((MailAddress) o).getName());
                    form.setNickname(((MailAddress) o).getNickname());
                    form.setOwnFlag(((MailAddress) o).getOwnFlag());
                    form.setUserid(String.valueOf(userid));
                    form.setPosition(((MailAddress) o).getPosition());
                    form.setSurname(((MailAddress) o).getSurname());
                    form.setTelephone(((MailAddress) o).getTelephone());
                    addresses.add(form);
                }
            }
        }
        return addresses;
    }

    /**
     * ����û������е�ַ����Ŀ����
     * @param mailUser MailUser
     * @throws Exception �����?���׳��쳣
     * @return Collection
     */
    protected Collection getAllOfUser(MailUser mailUser) throws Exception {
        if (mailUser == null) {
            return null;
        }
        return new UserDaoHibernate(mailUser).getAddresses();
    }

    private AddressForm convertVoToForm(MailAddress vo) {
        AddressForm form = new AddressForm();
        form.setCompanyaddress(vo.getCompanyAddress());
        form.setCompanyname(vo.getCompanyName());
        form.setEmail(vo.getMailAddress());
        form.setId(String.valueOf(vo.getId()));
        form.setHomeaddress(vo.getFamilyAddress());
        form.setMobile(vo.getMobilePhone());
        form.setDirectoryId(vo.getDirectoryId());
        form.setName(vo.getName());
        form.setNickname(vo.getNickname());
        form.setOwnFlag(vo.getOwnFlag());
        form.setUserid(vo.getUserId() == 0L ? "" : String.valueOf(vo.getUserId()));
        form.setPosition(vo.getPosition());
        form.setSurname(vo.getSurname());
        form.setTelephone(vo.getTelephone());
        return form;
    }

    public Collection getCurrentDirAddress(long userid, long directId) throws Exception {
        Collection collect = null;
        Collection vRet = new Vector();
        try {
            collect = getAllAddressOfUser(userid);
            Iterator it = collect.iterator();
            while (it.hasNext()) {
                AddressForm aform = (AddressForm) it.next();
                logger.debug(aform.getDirectoryId() + "****getDirectoryId***");
                logger.debug(directId + "****directId***");
                if (aform.getDirectoryId() == directId) {
                    vRet.add(aform);
                }
            }
        } catch (Exception e) {
        }
        logger.debug("vc size ======" + vRet.size());
        return vRet;
    }

    public Collection getDefaultDirAddress(long userid, long directId) throws Exception {
        Collection collect = null;
        Collection vRet = new Vector();
        try {
            collect = getAllAddressOfUser(userid);
            logger.debug("collect.size() == " + collect.size());
            Iterator it = collect.iterator();
            while (it.hasNext()) {
                AddressForm aform = (AddressForm) it.next();
                if (aform.getOwnFlag().equals(AddressFacade.PRIVATE_ADDRESS)) {
                    vRet.add(aform);
                }
            }
        } catch (Exception e) {
        }
        logger.debug("vRet.size() == " + vRet.size());
        return vRet;
    }

    public void deleteByDirectoryId(long userId, List collectDir) throws Exception {
        Collection collect = this.getAllOfUser(userId);
        Iterator it = collect.iterator();
        Collection delCollect = new ArrayList();
        AddressBookDao abd = new AddressBookDaoHibernate();
        Iterator itDirectory = collectDir.iterator();
        while (itDirectory.hasNext()) {
            long directoryId = Long.parseLong((String) (itDirectory.next()));
            while (it.hasNext()) {
                MailAddress maddr = (MailAddress) it.next();
                if (maddr.getDirectoryId() == directoryId) {
                    delCollect.add(maddr);
                }
            }
        }
        abd.removeMailAddressBatch(delCollect);
    }

    /**
	 * ������е�ַ����Ŀ����
	 * @throws Exception �����?���׳��쳣
	 * @return HashMap{
	 * 		{"userId||directorId",ArrayList{String[]{name,email}}},...
	 * 	}
	 */
    public HashMap getAllOfUser() throws Exception {
        HashMap hmReturn = new HashMap();
        List addre = (List) new UserDaoHibernate().getAllAddresses();
        if (null != addre || !addre.isEmpty()) {
            Iterator it = addre.iterator();
            String userId, directorId, sName, sEmail, sKey;
            MailAddress mailAddre;
            ArrayList alTemp;
            while (it.hasNext()) {
                mailAddre = (MailAddress) it.next();
                userId = String.valueOf(mailAddre.getUserId());
                directorId = String.valueOf(mailAddre.getDirectoryId());
                sName = mailAddre.getName();
                sEmail = mailAddre.getMailAddress();
                sKey = userId + "||" + directorId;
                alTemp = (ArrayList) hmReturn.get(sKey);
                if (null == alTemp) alTemp = new ArrayList();
                alTemp.add(new String[] { sName, sEmail });
                hmReturn.put(sKey, alTemp);
            }
        }
        return hmReturn;
    }
}
