package org.jgentleframework.services.eventservices.objectmeta;

/**
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Oct 27, 2007
 */
public interface ObjectEvent {

    /**
	 * Thiết lập subscriptions.
	 * <p>
	 * <b>Lưu ý:</b> thứ tự các tên định danh subscribers được chỉ định sẽ
	 * chính là thứ tự sẽ thực thi các subscribers khi event được ném ra bởi
	 * publisher nếu tham số inParallel là false.
	 * 
	 * @param subscriberNames
	 *            danh sách tên định danh các subscribers chỉ định.
	 * @param inParallel
	 *            Nếu là true thì khi event được ném ra, các subscriber sẽ đồng
	 *            loạt nhận được event và được chỉ định thực thi đồng loạt cùng
	 *            lúc. Nếu là false thì ngược lại, các subscriber sẽ thực thi
	 *            tuần tự theo thứ tự chỉ định của subscriberNames.
	 */
    public Subscription setSubscription(boolean inParallel, String... subscriberNames);

    /**
	 * Thiết lập subscriptions.
	 * <p>
	 * <b>Lưu ý:</b> thứ tự các tên định danh subscribers được chỉ định sẽ
	 * chính là thứ tự sẽ thực thi các subscribers khi event được ném ra bởi
	 * publisher.
	 * 
	 * @param subscriberNames
	 *            danh sách tên định danh các subscribers chỉ định.
	 */
    public Subscription setSubscription(String... subscriberNames);

    /**
	 * Trả về tên định danh name của event hiện hành
	 * 
	 * @return String
	 */
    public String getName();

    /**
	 * Thiết lập tên định danh name của event hiện hành.
	 * 
	 * @param name
	 *            tên định danh cần thiết lập.
	 */
    public void setName(String name);

    /**
	 * Trả về subscription của event hiện hành
	 * 
	 * @return trả về subscription nếu có, nếu không trả về null.
	 */
    public Subscription getSubscription();

    /**
	 * Thiết lập subscription cho event hiện hành.
	 * 
	 * @param subscription
	 *            đối tượng subscription cần thiết lập.
	 */
    public void setSubscription(Subscription subscription);
}
