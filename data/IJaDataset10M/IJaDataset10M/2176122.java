package icapture.com;

/**
 *
 * @author ATan1
 */
public final class ViewShipmentManager {

    private int first;

    private UserHttpSess parent;

    private int step;

    private int subjectCount;

    private int total;

    public ViewShipmentManager(UserHttpSess tmpHttpSessObj) {
        parent = tmpHttpSessObj;
        step = 0;
        total = -1;
        first = 0;
    }

    public void clearValues() {
        total = -1;
        first = 0;
        step = parent.getViewLimit();
    }

    public void changeShipmentTotal(int diff) {
        total += diff;
    }

    public void setShipmentCount(int val) {
        total = val;
    }

    public boolean hasShipmentPrevious() {
        return (step > 0) && (first > 0);
    }

    public void setShipmentPrevious() {
        first -= step;
        if (first < 0) {
            first = 0;
        }
    }

    public boolean hasShipmentNext() {
        return (step > 0) && (first + step < total - 1);
    }

    public void setShipmentNext() {
        first += step;
        if (first >= total) {
            setShipmentPrevious();
        }
    }

    public void setShipmentFirst() {
        first = 0;
    }

    public void setShipmentLast() {
        first = total - step;
        if (first < 0) {
            first = 0;
        }
    }

    public int getShipmentStep() {
        return step;
    }

    public int getShipmentCurrent() {
        return first;
    }

    public String getShipmentControlString() {
        StringBuffer xyz = new StringBuffer();
        if ((step > 0) && (total > step)) {
            if (first > 0) {
                xyz.append("<button type='button' onclick='gotoFirst()' title='first' class='lines'>").append("<img border='0' src='./images/left_d_10_b.gif' title='first' alt=''>").append("</button>");
                xyz.append("<button type='button' onclick='gotoPrevious()' title='previous' class='lines'>").append("<img border='0' src='./images/left_10_b.gif' title='previous' alt=''>").append("</button>");
            } else {
                xyz.append("<button type='button' disabled class='lines'>").append("<img border='0' src='./images/left_d_10_h.gif' alt=''>").append("</button>");
                xyz.append("<button type='button' disabled class='lines'>").append("<img border='0' src='./images/left_10_h.gif' alt=''>").append("</button>");
            }
            xyz.append(first + 1).append('-').append(first + step).append("&nbsp;of&nbsp;").append(total).append("&nbsp;items&nbsp;");
            if (first + step < total) {
                xyz.append("<button type='button' onclick='gotoNext()' title='next' class='lines'>").append("<img border='0' src='./images/right_10_b.gif' title='next' alt=''>").append("</button>");
                xyz.append("<button type='button' onclick='gotoLast()' title='last' class='lines'>").append("<img border='0' src='./images/right_d_10_b.gif' title='last' alt=''>").append("</button>");
            } else {
                xyz.append("<button type='button' disabled class='lines'>").append("<img border='0' src='./images/right_10_h.gif' alt=''>").append("</button>");
                xyz.append("<button type='button' disabled class='lines'>").append("<img border='0' src='./images/right_d_10_h.gif' alt=''>").append("</button>");
            }
        } else {
            xyz.append(total).append("&nbsp;items&nbsp;");
        }
        return xyz.toString();
    }
}
