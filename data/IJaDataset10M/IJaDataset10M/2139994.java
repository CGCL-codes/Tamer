package mail.util;

import mail.exceptions.MimeMessageHeaderAddressValidationException;

public class AddressParser {

    enum AddressParserStates {

        IN_ROUTE_ADDRESS, OUT_ROUTE_ADDRESS, AFTER_ROUTE_ADDRESS
    }

    /**
	 * Validates addresses according to RFC822 - semantic above
	 * no line folded in stringAddress!!!
	 * also validates multipule addresses splited by comma ,
	 * f.e. 1#address
	 * 
	 * eg.
	  -- 
	   "Zbigniew Artemiuk" <zbigniew.artemiuk@gmail.com>
	  -- 
	   Zbigniew Artemiuk <zbychu@astronet.pl>,
        Zbigniew Artemiuk <zbychu@astronet.pl>
        
	 * @param address without line folded
	 */
    public static void validateAddress(String stringAddress) {
        AddressParserStates routeAddressState = AddressParserStates.OUT_ROUTE_ADDRESS;
        int spaceCounter = 0;
        int quotedStringCounter = 0;
        boolean quotedText = false;
        boolean wasPhrase = false;
        boolean dotEncounter = false;
        boolean atEncounter = false;
        boolean atAppeared = false;
        char beforeChar = " ".charAt(0);
        for (int i = 0; i < stringAddress.length(); i++) {
            char c = stringAddress.charAt(i);
            if (routeAddressState == AddressParserStates.AFTER_ROUTE_ADDRESS) {
                if (c == 44) {
                    routeAddressState = AddressParserStates.OUT_ROUTE_ADDRESS;
                    wasPhrase = false;
                    atEncounter = false;
                    atAppeared = false;
                    spaceCounter = 0;
                    quotedStringCounter = 0;
                    continue;
                }
                String reason = "No " + c + " available at this stage";
                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
            } else if (c == 32) {
                if (!quotedText) {
                    if (routeAddressState == AddressParserStates.OUT_ROUTE_ADDRESS) {
                        if (spaceCounter > 1) {
                            String reason = "Space outside qouted string - maybe mail from Thunderbird";
                            throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                        }
                        spaceCounter++;
                        wasPhrase = true;
                    } else {
                        String reason = "Space inside address";
                        throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                    }
                    spaceCounter++;
                }
            } else if (StringUtils.isSpecialCharacter(c)) {
                if (!quotedText) {
                    if (dotEncounter || atEncounter) {
                        if (dotEncounter) {
                            String reason = "Special char " + c + " not allowed after .";
                            throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                        } else if (atEncounter) {
                            if (c == 91) {
                            } else if (c == 91) {
                            } else {
                                String reason = "Special char " + c + " not allowed after @";
                                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                            }
                        }
                    } else {
                        if (c == 60) {
                            if (beforeChar != 32) {
                                String reason = "" + c + " without space before";
                                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                            } else if (routeAddressState == AddressParserStates.IN_ROUTE_ADDRESS) {
                                String reason = c + "not allowed on this stage - in route address";
                                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                            } else {
                                routeAddressState = AddressParserStates.IN_ROUTE_ADDRESS;
                            }
                        } else if (c == 62) {
                            if (routeAddressState == AddressParserStates.IN_ROUTE_ADDRESS) {
                                if (beforeChar == 60) {
                                    System.out.println("Nothing insde <>");
                                }
                                if (!atAppeared) {
                                    String reason = "@ not in address - should be instead of " + c;
                                    throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                                }
                                routeAddressState = AddressParserStates.AFTER_ROUTE_ADDRESS;
                            } else {
                                String reason = "" + c + " without < before";
                                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                            }
                        } else if (c == 64) {
                            if (StringUtils.isSpecialCharacter(beforeChar) || StringUtils.isCTLCharacter(beforeChar) || beforeChar == 32 || beforeChar == 46) {
                                String reason = "Not allowed char " + c + " before @";
                                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                            } else if (atAppeared) {
                                String reason = "Second encounter of @";
                                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                            } else if (routeAddressState == AddressParserStates.OUT_ROUTE_ADDRESS && wasPhrase) {
                                String reason = "@ cannot encounter on this stage";
                                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                            } else {
                                atEncounter = true;
                                atAppeared = true;
                                beforeChar = c;
                                continue;
                            }
                        } else if (c == 34) {
                            if (beforeChar != 92) {
                                quotedStringCounter++;
                                quotedText = !quotedText;
                            }
                        } else if (c == 46) {
                            if (StringUtils.isSpecialCharacter(beforeChar) || StringUtils.isCTLCharacter(beforeChar) || beforeChar == 32) {
                                String reason = "Wrong char " + beforeChar + " before .";
                                throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                            } else {
                                dotEncounter = true;
                                beforeChar = c;
                                continue;
                            }
                        } else {
                            String reason = "Special character " + c + " outside qouted string";
                            throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                        }
                    }
                } else {
                    if (c == 34) {
                        if (beforeChar != 92) {
                            quotedStringCounter++;
                            quotedText = !quotedText;
                        }
                    }
                }
            } else if (StringUtils.isCTLCharacter(c)) {
                if (!quotedText) {
                    String reason = "CTL character " + c + " outside qouted string";
                    throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                } else {
                    if (dotEncounter) {
                        String reason = "CTL char" + c + "not allowed after .";
                        throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                    } else if (atEncounter) {
                        String reason = "CTL char" + c + "not allowed after @";
                        throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                    } else {
                        if ((c == 92 || c == 34 || c == 13) && beforeChar != 92) {
                            String reason = "No " + c + " available here without escaping by \\";
                            throw new MimeMessageHeaderAddressValidationException(stringAddress, reason, i);
                        }
                    }
                }
            } else {
            }
            beforeChar = c;
            dotEncounter = false;
            atEncounter = false;
        }
        if (routeAddressState != AddressParserStates.AFTER_ROUTE_ADDRESS && !atAppeared) {
            String reason = "No address in mail";
            throw new MimeMessageHeaderAddressValidationException(reason);
        } else if (dotEncounter || atEncounter) {
            String reason = "No @ or . available at the end of email";
            throw new MimeMessageHeaderAddressValidationException(reason);
        }
        if (quotedStringCounter % 2 != 0) {
            String reason = "Number of \" is wrong";
            throw new MimeMessageHeaderAddressValidationException(reason);
        }
    }

    public static void main(String[] args) {
        String address = "\"Zbigniew \" Artemiuk\" <s@s>";
        String address2 = "zbychu@fds\fds.pl";
        try {
            validateAddress(address);
        } catch (MimeMessageHeaderAddressValidationException ex) {
            ex.printStackTrace();
        }
    }
}
