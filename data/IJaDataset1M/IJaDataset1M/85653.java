package org.broadleafcommerce.rating.domain;

import java.util.Date;
import java.util.List;
import org.broadleafcommerce.profile.domain.Customer;
import org.broadleafcommerce.rating.service.type.ReviewStatusType;

public interface ReviewDetail {

    public Long getId();

    public Customer getCustomer();

    public String getReviewText();

    public Date getReviewSubmittedDate();

    public Integer helpfulCount();

    public Integer notHelpfulCount();

    public ReviewStatusType getStatus();

    public RatingSummary getRatingSummary();

    public List<ReviewFeedback> getReviewFeedback();
}
