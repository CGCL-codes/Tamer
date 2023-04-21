package org.owasp.esapi.swingset.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.esapi.errors.EnterpriseSecurityException;
import org.owasp.esapi.reference.DefaultRandomizer;

public class RandomnessSolution {

    public static void invoke(HttpServletRequest request, HttpServletResponse response) throws EnterpriseSecurityException {
        int rangeValue = 0;
        int randomNum = 0;
        try {
            if (request.getParameter("rangeVal") != null) {
                rangeValue = Integer.parseInt(request.getParameter("rangeVal"));
            }
            DefaultRandomizer randomizer = new DefaultRandomizer();
            randomNum = randomizer.getRandomInteger(0, rangeValue);
            request.setAttribute("randomNum", randomNum);
            request.setAttribute("rangeValue", rangeValue);
        } catch (Exception e) {
            request.setAttribute("randomNum", "Please enter an integer between 0 and 2147483647 in the box above.");
        }
    }
}
